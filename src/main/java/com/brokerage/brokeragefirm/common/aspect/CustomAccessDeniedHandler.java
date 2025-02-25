package com.brokerage.brokeragefirm.common.aspect;

import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.exception.PermissionException;
import com.brokerage.brokeragefirm.common.mapper.CustomExceptionResponseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var errorResponse = CustomExceptionResponseMapper.toResponse(new PermissionException(Error.PERMISSION_DENIED));

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}