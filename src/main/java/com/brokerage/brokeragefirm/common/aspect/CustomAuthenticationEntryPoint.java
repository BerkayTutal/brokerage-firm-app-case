package com.brokerage.brokeragefirm.common.aspect;

import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.mapper.CustomExceptionResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var errorResponse = new com.brokerage.brokeragefirm.common.exception.AuthenticationException(Error.UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(CustomExceptionResponseMapper.toResponse(errorResponse)));
    }
}