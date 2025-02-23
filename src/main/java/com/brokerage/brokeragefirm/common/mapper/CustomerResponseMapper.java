package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.rest.dto.CustomerResponse;
import com.brokerage.brokeragefirm.service.model.Customer;

public class CustomerResponseMapper {
    public static CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .roles(customer.getRoles())
                .build();
    }
}
