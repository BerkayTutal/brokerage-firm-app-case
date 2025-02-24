package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.common.exception.CustomException;
import com.brokerage.brokeragefirm.rest.dto.CustomExceptionResponse;

public class CustomExceptionResponseMapper {
    public static CustomExceptionResponse toResponse(CustomException exception) {
        return new CustomExceptionResponse(exception.getMessage());
    }
}
