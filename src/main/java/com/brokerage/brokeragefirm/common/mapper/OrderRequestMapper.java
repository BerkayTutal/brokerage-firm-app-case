package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.rest.dto.OrderRequest;
import com.brokerage.brokeragefirm.service.model.Order;

public class OrderRequestMapper {

    public static Order toModel(OrderRequest orderRequest) {
        return Order.builder()
                .customerId(orderRequest.customerId())
                .assetName(orderRequest.assetName())
                .orderSide(orderRequest.orderSide())
                .size(orderRequest.size())
                .price(orderRequest.price())
                .build();
    }
}
