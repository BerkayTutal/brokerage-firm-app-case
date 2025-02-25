package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Order;
import jakarta.transaction.Transactional;

import java.util.List;

public interface OrderService {
    Order create(Order order);

    @Transactional
    Order match(Long orderId);

    List<Order> getAll();

    Order get(Long orderId);

    Order cancel(Long orderId);

    List<Order> getAll(Long customerId);
}
