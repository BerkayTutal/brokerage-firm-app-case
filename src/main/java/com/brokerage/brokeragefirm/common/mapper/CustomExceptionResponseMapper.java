package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.common.exception.CustomException;
import com.brokerage.brokeragefirm.rest.dto.CustomExceptionResponse;

public class CustomExceptionResponseMapper {
    public static CustomExceptionResponse toResponse(CustomException exception) {
        return CustomExceptionResponse.builder()
                .message(exception.getMessage())
                .build();
    }
}
