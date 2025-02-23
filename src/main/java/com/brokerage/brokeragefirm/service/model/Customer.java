package com.brokerage.brokeragefirm.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class Customer {
    private Long id;
    private String email;
    private String password;
    private Set<Role> roles;
    private List<Asset> assets;
    private List<Order> orders;
}