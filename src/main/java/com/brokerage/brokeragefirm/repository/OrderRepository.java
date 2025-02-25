package com.brokerage.brokeragefirm.repository;

import com.brokerage.brokeragefirm.repository.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findAllByCustomerId(Long customerId, Pageable pageable);
}
