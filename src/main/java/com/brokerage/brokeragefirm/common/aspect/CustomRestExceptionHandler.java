package com.brokerage.brokeragefirm.common.aspect;

import com.brokerage.brokeragefirm.common.exception.*;
import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.mapper.CustomExceptionResponseMapper;
import com.brokerage.brokeragefirm.rest.dto.CustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class CustomRestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomExceptionResponse handleNotFoundException(CustomException ce) {
        return CustomExceptionResponseMapper.toResponse(ce);
    }

    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CustomExceptionResponse handlePermissionException(CustomException ce) {
        return CustomExceptionResponseMapper.toResponse(ce);
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CustomExceptionResponse handleOperationNotAllowedException(CustomException ce) {
        return CustomExceptionResponseMapper.toResponse(ce);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CustomExceptionResponse handleDuplicateEntryException(CustomException ce) {
        return CustomExceptionResponseMapper.toResponse(ce);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CustomExceptionResponse handleAuthenticationException(CustomException ce) {
        return CustomExceptionResponseMapper.toResponse(ce);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation errors: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(String.format("%s: %s; ", error.getField(), error.getDefaultMessage()));
        }
        return CustomExceptionResponseMapper.toResponse(new OperationNotAllowedException(Error.VALIDATION_ERROR, errorMessage.toString()));
    }

}
