package com.brokerage.brokeragefirm.service;

import com.brokerage.brokeragefirm.service.model.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerService {
    Customer registerCustomer(Customer customer);

    Customer getCustomerByEmail(String email);

    Customer getCustomerById(Long id);

    @Transactional
    Customer updateEmailPassword(Customer customer);

    List<Customer> getAllCustomers();

    boolean existsById(Long id);
}
