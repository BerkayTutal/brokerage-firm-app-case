package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.rest.dto.CustomerRequest;
import com.brokerage.brokeragefirm.service.model.Customer;

public class CustomerRequestMapper {
    public static Customer toModel(CustomerRequest customerRequest, Long id) {
        return Customer.builder()
                .id(id)
                .email(customerRequest.getEmail())
                .password(customerRequest.getPassword())
                .build();
    }
}
