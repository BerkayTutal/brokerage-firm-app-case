package com.brokerage.brokeragefirm.common.exception;

import com.brokerage.brokeragefirm.common.enums.Error;

public class AuthenticationException extends CustomException {
    public AuthenticationException(Error error, Object... params) {
        super(error, params);
    }
}
