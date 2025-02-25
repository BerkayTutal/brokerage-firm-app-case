package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Order create(Order order);

    @Transactional
    Order match(Long orderId);

    Page<Order> getAll(Pageable pageable);

    Page<Order> getAll(Long customerId, Pageable pageable);

    Order get(Long orderId);

    Order cancel(Long orderId);
}
