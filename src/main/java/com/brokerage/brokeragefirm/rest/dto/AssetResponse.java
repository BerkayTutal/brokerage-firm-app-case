package com.brokerage.brokeragefirm.rest.dto;

import java.math.BigDecimal;

public record AssetResponse(
        Long id,
        Long customerId,
        String assetName,
        BigDecimal size,
        BigDecimal usableSize
) {
}