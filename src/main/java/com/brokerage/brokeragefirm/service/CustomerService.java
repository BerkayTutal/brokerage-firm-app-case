package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerService {
    Customer register(Customer customer);

    Customer get(String email);

    Customer get(Long id);

    @Transactional
    Customer updateEmailPassword(Customer customer);

    Page<Customer> getAll(Pageable pageable);

    boolean exists(Long id);
}
