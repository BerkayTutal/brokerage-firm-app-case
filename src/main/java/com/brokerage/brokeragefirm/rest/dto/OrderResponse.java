package com.brokerage.brokeragefirm.rest.dto;

import com.brokerage.brokeragefirm.common.enums.Side;
import com.brokerage.brokeragefirm.common.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long customerId,
        String assetName,
        Side orderSide,
        BigDecimal size,
        BigDecimal price,
        Status status,
        LocalDateTime createDate,
        LocalDateTime updateDate
) {
}