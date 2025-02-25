package com.brokerage.brokeragefirm.service.model;

import java.math.BigDecimal;

public record Transaction(
        Long customerId,
        String assetName,
        BigDecimal amount) {
}
