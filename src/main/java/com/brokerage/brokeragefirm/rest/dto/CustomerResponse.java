package com.brokerage.brokeragefirm.rest.dto;

import com.brokerage.brokeragefirm.service.model.Role;

import java.util.Set;

public record CustomerResponse(
        Long id,
        String email,
        Set<Role> roles
) {
}
