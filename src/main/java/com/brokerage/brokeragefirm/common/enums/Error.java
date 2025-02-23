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
    EMAIL_ALREADY_EXISTS("Email already exists!"),
    ASSET_ALREADY_EXISTS("%s asset with id %s already exists"),
    ASSET_NOT_FOUND_ID("Asset not found with id: %s"),
    ASSET_NOT_FOUND_ASSET_CUSTOMER("%s asset not found for customerId %s"),
    INSUFFICIENT_FUNDS("Insufficient %s for customerId %s");

    private final String errorDescription;

}
