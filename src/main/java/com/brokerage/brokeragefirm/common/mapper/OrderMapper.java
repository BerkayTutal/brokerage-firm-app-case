package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.repository.entity.OrderEntity;
import com.brokerage.brokeragefirm.service.model.Order;

public class OrderMapper {
    public static Order toModel(OrderEntity orderEntity) {
        return Order.builder()
                .id(orderEntity.getId())
                .customerId(orderEntity.getCustomer().getId())
                .assetName(orderEntity.getAssetName())
                .orderSide(orderEntity.getOrderSide())
                .size(orderEntity.getSize())
                .price(orderEntity.getPrice())
                .status(orderEntity.getStatus())
                .createDate(orderEntity.getCreateDate())
                .updateDate(orderEntity.getUpdateDate())
                .build();
    }

    public static OrderEntity toEntity(Order order) {
        return OrderEntity.builder()
                .id(order.getId())
                .customer(CustomerEntity.builder().id(order.getCustomerId()).build())
                .assetName(order.getAssetName())
                .orderSide(order.getOrderSide())
                .size(order.getSize())
                .price(order.getPrice())
                .status(order.getStatus())
                .build();
    }
}
