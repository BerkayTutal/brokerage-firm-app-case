package com.brokerage.brokeragefirm.common.mapper;

import com.brokerage.brokeragefirm.common.exception.CustomException;
import com.brokerage.brokeragefirm.rest.dto.CustomExceptionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomExceptionResponseMapperTest {

    @Test
    void toResponse_ValidCustomException_ReturnsCorrectResponse() {
        // given
        CustomException exception = mock(CustomException.class);

        when(exception.getMessage()).thenReturn("This is a test message");

        // when
        CustomExceptionResponse response = CustomExceptionResponseMapper.toResponse(exception);

        // then
        assertEquals("This is a test message", response.message());
    }

}