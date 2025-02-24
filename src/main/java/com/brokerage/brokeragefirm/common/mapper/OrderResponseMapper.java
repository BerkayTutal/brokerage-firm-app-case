package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.rest.dto.OrderResponse;
import com.brokerage.brokeragefirm.service.model.Order;

public class OrderResponseMapper {

    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(order.getId(),
                order.getCustomerId(),
                order.getAssetName(),
                order.getOrderSide(),
                order.getSize(),
                order.getPrice(),
                order.getStatus(),
                order.getCreateDate(),
                order.getUpdateDate()
        );
    }
}
