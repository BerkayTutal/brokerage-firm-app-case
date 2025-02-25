package com.brokerage.brokeragefirm.rest.dto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record TransactionRequest(
        @DecimalMin("0") BigDecimal amount) {
}