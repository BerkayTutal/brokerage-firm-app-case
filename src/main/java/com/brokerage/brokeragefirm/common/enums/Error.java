package com.brokerage.brokeragefirm.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {
    CUSTOMER_NOT_FOUND_ID("Customer not found with id: %s"),
    CUSTOMER_NOT_FOUND_EMAIL("Customer not found with email: %s"),
    INCORRECT_EMAIL_PASSWORD("Incorrect email or password."),
    NO_PERMISSION_CUSTOMER("Logged customer is different than you are trying to access!"),
    NO_PERMISSION_ASSET("Logged user has no permission to access this asset!"),
    NO_PERMISSION_ORDER("Logged user has no permission to access this order!"),
    EMAIL_ALREADY_EXISTS("Email already exists!"),
    ASSET_ALREADY_EXISTS("User with id %s already has %s asset!"),
    ASSET_NOT_FOUND_ID("Asset not found with id: %s"),
    ASSET_NOT_FOUND_ASSET_CUSTOMER("%s asset not found for customerId %s"),
    INSUFFICIENT_FUNDS("Insufficient %s for customerId %s"),
    ORDER_NOT_FOUND_ID("Order not found with id: %s");

    private final String errorDescription;

}
