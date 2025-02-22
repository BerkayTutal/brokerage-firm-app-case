package com.brokerage.brokeragefirm.rest.dto;

import lombok.*;

@Data
@Builder
public class CustomExceptionResponse {
    private String message;

    public static CustomExceptionResponse from(RuntimeException exception) {
        return CustomExceptionResponse.builder()
                .message(exception.getMessage())
                .build();
    }
}

