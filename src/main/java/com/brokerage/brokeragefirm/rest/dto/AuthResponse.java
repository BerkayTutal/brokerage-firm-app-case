package com.brokerage.brokeragefirm.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthResponse {
    private final Long userId;
    private final String token;
}
