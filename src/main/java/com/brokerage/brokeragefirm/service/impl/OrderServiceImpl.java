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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AssetService assetService;
    private final CustomerService customerService;

    @Transactional
    @Override
    public Order create(Order order) {

        if (Constants.ASSET_TRY.equals(order.getAssetName())) {
            throw new OperationNotAllowedException(Error.ASSET_TRY_NOT_ALLOWED);
        }

        if (Side.BUY.equals(order.getOrderSide())) {
            Asset tryAsset = assetService.get(order.getCustomerId(), Constants.ASSET_TRY);
            BigDecimal usableSize = tryAsset.getUsableSize().subtract(order.getSize().multiply(order.getPrice()));
            if (usableSize.compareTo(BigDecimal.ZERO) < 0) {
                throw new OperationNotAllowedException(Error.INSUFFICENT_ASSET, Constants.ASSET_TRY, order.getOrderSide(), order.getAssetName());
            }
            tryAsset.setUsableSize(usableSize);
            assetService.update(tryAsset);
        } else {
            Asset sellingAsset = assetService.get(order.getCustomerId(), order.getAssetName());
            BigDecimal usableSize = sellingAsset.getUsableSize().subtract(order.getSize());
            if (usableSize.compareTo(BigDecimal.ZERO) < 0) {
                throw new OperationNotAllowedException(Error.INSUFFICENT_ASSET, order.getAssetName(), order.getOrderSide(), order.getAssetName());
            }
            sellingAsset.setUsableSize(usableSize);
            assetService.update(sellingAsset);
        }

        OrderEntity orderEntity = OrderMapper.toEntity(order);
        orderEntity.setStatus(Status.PENDING);

        return OrderMapper.toModel(orderRepository.save(orderEntity));
    }

    @Transactional
    @Override
    public Order match(Long orderId) {

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

        return OrderMapper.toModel(orderRepository.save(orderEntity));
    }


    @Override
    public List<Order> getAll() {
        return orderRepository.findAll().stream().map(OrderMapper::toModel).toList();
    }

    @Override
    public Order get(Long orderId) {
        return orderRepository.findById(orderId).map(OrderMapper::toModel)
                .orElseThrow(() -> new NotFoundException(Error.ORDER_NOT_FOUND_ID, orderId));
    }

    @Transactional
    @Override
    public Order cancel(Long orderId) {
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

            return OrderMapper.toModel(orderRepository.save(orderEntity));
        } else {
            throw new OperationNotAllowedException(Error.NOT_ALLOWED_STATE_CHANGE, Status.PENDING, orderEntity.getStatus());
        }
    }

    @Override
    public List<Order> getAll(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId).stream().map(OrderMapper::toModel).toList();
    }
}
