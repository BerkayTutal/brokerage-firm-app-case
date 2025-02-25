package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerService {
    Customer register(Customer customer);

    Customer get(String email);

    Customer get(Long id);

    @Transactional
    Customer updateEmailPassword(Customer customer);

    List<Customer> getAll();

    boolean exists(Long id);
}
