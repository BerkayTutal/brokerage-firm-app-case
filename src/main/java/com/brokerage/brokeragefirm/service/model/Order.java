package com.brokerage.brokeragefirm.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Order {
    private Long id;
    private String status;
    private Double amount;
    private Customer customer;
}
