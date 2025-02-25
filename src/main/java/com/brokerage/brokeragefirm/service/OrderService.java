package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Order;
import jakarta.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);

    @Transactional
    Order matchOrder(Long orderId);

    List<Order> getAllOrders();

    Order getOrder(Long orderId);

    Order cancelOrder(Long orderId);

    List<Order> getOrdersByCustomerId(Long customerId);

    boolean existsById(Long orderId);
}
