package com.brokerage.brokeragefirm.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AssetRequest(
        Long id,
        @NotNull Long customerId,
        @NotNull String assetName,
        @NotNull @DecimalMin("0") BigDecimal size,
        @NotNull @DecimalMin("0") BigDecimal usableSize
) {
}