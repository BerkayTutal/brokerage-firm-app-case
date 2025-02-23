package com.brokerage.brokeragefirm.service.model;

import com.brokerage.brokeragefirm.common.enums.Side;
import com.brokerage.brokeragefirm.common.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Order {
    private Long id;
    private Customer customer;
    private String assetName;
    private Side side;
    private BigDecimal size;
    private BigDecimal price;
    private Status status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
