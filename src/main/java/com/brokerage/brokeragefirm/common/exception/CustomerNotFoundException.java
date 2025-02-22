package com.brokerage.brokeragefirm.common.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long customerId) {
        super("Customer not found with id: " + customerId);
    }

}
