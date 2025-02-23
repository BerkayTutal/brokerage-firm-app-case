package com.brokerage.brokeragefirm.common.exception;

import com.brokerage.brokeragefirm.common.enums.Error;
import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    public CustomException(Error error, Object... params) {
        super(String.format(error.getErrorDescription(), params));
    }
}
