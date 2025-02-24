package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Order;

import java.util.Arrays;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);

    List<Order> getAllOrders();

    Order getOrder(Long orderId);

    Order cancelOrder(Long orderId);

    List<Order> getOrdersByCustomerId(Long customerId);
}
