package com.brokerage.brokeragefirm.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank String email,
        @NotBlank String password
) {
}
