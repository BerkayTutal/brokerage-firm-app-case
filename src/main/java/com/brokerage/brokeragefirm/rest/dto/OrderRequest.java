package com.brokerage.brokeragefirm.rest.dto;

import com.brokerage.brokeragefirm.common.enums.Side;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderRequest(
        @NotNull Long customerId,
        @NotBlank String assetName,
        @NotNull Side orderSide,
        @Min(value = 0) BigDecimal size,
        @Min(value = 0) BigDecimal price
) {
}
