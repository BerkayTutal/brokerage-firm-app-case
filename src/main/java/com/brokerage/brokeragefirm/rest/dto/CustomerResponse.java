package com.brokerage.brokeragefirm.rest.dto;

import com.brokerage.brokeragefirm.service.model.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class CustomerResponse {
    private final Long id;
    private final String email;
    private final Set<Role> roles;

}
