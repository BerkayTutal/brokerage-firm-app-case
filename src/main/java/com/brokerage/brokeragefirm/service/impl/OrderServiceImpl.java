package com.brokerage.brokeragefirm.service.impl;

import com.brokerage.brokeragefirm.common.constants.Constants;
import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.enums.Side;
import com.brokerage.brokeragefirm.common.enums.Status;
import com.brokerage.brokeragefirm.common.exception.NotFoundException;
import com.brokerage.brokeragefirm.common.exception.OperationNotAllowedException;
import com.brokerage.brokeragefirm.common.mapper.OrderMapper;
import com.brokerage.brokeragefirm.repository.OrderRepository;
import com.brokerage.brokeragefirm.repository.entity.OrderEntity;
import com.brokerage.brokeragefirm.service.AssetService;
import com.brokerage.brokeragefirm.service.CustomerService;
import com.brokerage.brokeragefirm.service.OrderService;
import com.brokerage.brokeragefirm.service.model.Asset;
import com.brokerage.brokeragefirm.service.model.Order;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AssetService assetService;
    private final CustomerService customerService;

    @Transactional
    @Override
    public Order create(Order order) {
        log.info("Creating a new order: {}", order);

        if (Constants.ASSET_TRY.equals(order.getAssetName())) {
            throw new OperationNotAllowedException(Error.ASSET_TRY_NOT_ALLOWED);
        }

        if (Side.BUY.equals(order.getOrderSide())) {
            Asset tryAsset = assetService.get(order.getCustomerId(), Constants.ASSET_TRY);
            BigDecimal usableSize = tryAsset.getUsableSize().subtract(order.getSize().multiply(order.getPrice()));
            if (usableSize.compareTo(BigDecimal.ZERO) < 0) {
                throw new OperationNotAllowedException(Error.INSUFFICIENT_ASSET, Constants.ASSET_TRY, order.getOrderSide(), order.getAssetName());
            }
            tryAsset.setUsableSize(usableSize);
            assetService.update(tryAsset);
        } else {
            Asset sellingAsset = assetService.get(order.getCustomerId(), order.getAssetName());
            BigDecimal usableSize = sellingAsset.getUsableSize().subtract(order.getSize());
            if (usableSize.compareTo(BigDecimal.ZERO) < 0) {
                throw new OperationNotAllowedException(Error.INSUFFICIENT_ASSET, order.getAssetName(), order.getOrderSide(), order.getAssetName());
            }
            sellingAsset.setUsableSize(usableSize);
            assetService.update(sellingAsset);
        }

        OrderEntity orderEntity = OrderMapper.toEntity(order);
        orderEntity.setStatus(Status.PENDING);

        Order savedOrder = OrderMapper.toModel(orderRepository.save(orderEntity));
        log.debug("Order created successfully: {}", savedOrder);
        return savedOrder;
    }

    @Transactional
    @Override
    public Order match(Long orderId) {
        log.info("Matching order with ID: {}", orderId);

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Error.ORDER_NOT_FOUND_ID, orderId));

        if (!orderEntity.getStatus().equals(Status.PENDING)) {
            throw new OperationNotAllowedException(Error.NOT_ALLOWED_STATE_CHANGE, orderEntity.getStatus(), Status.MATCHED);
        }
        Asset tryAsset = assetService.get(orderEntity.getCustomer().getId(), Constants.ASSET_TRY);

        if (orderEntity.getOrderSide().equals(Side.BUY)) {
            tryAsset.setSize(tryAsset.getSize().subtract(orderEntity.getSize().multiply(orderEntity.getPrice())));

            //Add bought asset or create from scratch
            if (assetService.exists(orderEntity.getCustomer().getId(), orderEntity.getAssetName())) {
                Asset boughtAsset = assetService.get(orderEntity.getCustomer().getId(), orderEntity.getAssetName());
                boughtAsset.setSize(boughtAsset.getSize().add(orderEntity.getSize()));
                boughtAsset.setUsableSize(boughtAsset.getUsableSize().add(orderEntity.getSize()));
                assetService.update(boughtAsset);
            } else {
                Asset newAsset = Asset.builder()
                        .customerId(orderEntity.getCustomer().getId())
                        .assetName(orderEntity.getAssetName())
                        .size(orderEntity.getSize())
                        .usableSize(orderEntity.getSize())
                        .build();
                assetService.create(newAsset);
            }

        } else {
            Asset sellingAsset = assetService.get(orderEntity.getCustomer().getId(), orderEntity.getAssetName());
            sellingAsset.setSize(sellingAsset.getSize().subtract(orderEntity.getSize()));
            assetService.update(sellingAsset);

            BigDecimal soldFor = orderEntity.getSize().multiply(orderEntity.getPrice());
            tryAsset.setSize(tryAsset.getSize().add(soldFor));
            tryAsset.setUsableSize(tryAsset.getUsableSize().add(soldFor));
        }
        assetService.update(tryAsset);

        orderEntity.setStatus(Status.MATCHED);

        Order matchedOrder = OrderMapper.toModel(orderRepository.save(orderEntity));
        log.debug("Order matched successfully: {}", matchedOrder);
        return matchedOrder;

    }

    @Override
    public Page<Order> getAll(Pageable pageable) {
        log.info("Fetching all orders with pageable: {}", pageable);
        return orderRepository.findAll(pageable).map(OrderMapper::toModel);
    }

    @Override
    public Page<Order> getAll(Long customerId, Pageable pageable) {
        log.info("Fetching all orders for customer ID: {} with pageable: {}", customerId, pageable);
        return orderRepository.findAllByCustomerId(customerId, pageable).map(OrderMapper::toModel);
    }


    @Override
    public Order get(Long orderId) {
        log.info("Fetching order with ID: {}", orderId);
        return orderRepository.findById(orderId).map(OrderMapper::toModel)
                .orElseThrow(() -> new NotFoundException(Error.ORDER_NOT_FOUND_ID, orderId));
    }

    @Transactional
    @Override
    public Order cancel(Long orderId) {
        log.info("Canceling order with ID: {}", orderId);

        //Check if the order exists
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Error.ORDER_NOT_FOUND_ID, orderId));

        //Check if the order is in the correct state
        if (orderEntity.getStatus().equals(Status.PENDING)) {

            //Change the order's status to "cancelled"
            orderEntity.setStatus(Status.CANCELLED);

            if (Side.BUY.equals(orderEntity.getOrderSide())) {
                Asset tryAsset = assetService.get(orderEntity.getCustomer().getId(), Constants.ASSET_TRY);
                tryAsset.setUsableSize(tryAsset.getUsableSize().add(orderEntity.getSize().multiply(orderEntity.getPrice())));
                assetService.update(tryAsset);
            } else {
                Asset sellingAsset = assetService.get(orderEntity.getCustomer().getId(), orderEntity.getAssetName());
                sellingAsset.setUsableSize(sellingAsset.getUsableSize().add(orderEntity.getSize()));
                assetService.update(sellingAsset);
            }
            Order canceledOrder = OrderMapper.toModel(orderRepository.save(orderEntity));
            log.debug("Order canceled successfully: {}", canceledOrder);
            return canceledOrder;
        } else {
            throw new OperationNotAllowedException(Error.NOT_ALLOWED_STATE_CHANGE, Status.PENDING, orderEntity.getStatus());
        }
    }

}
