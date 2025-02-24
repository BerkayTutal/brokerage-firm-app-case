package com.brokerage.brokeragefirm.service.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class Asset {
    private Long id;
    private Long customerId;
    private String assetName;
    @Builder.Default
    private BigDecimal size = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal usableSize = BigDecimal.ZERO;
}