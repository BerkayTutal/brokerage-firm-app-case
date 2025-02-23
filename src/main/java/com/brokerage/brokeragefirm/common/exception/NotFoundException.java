package com.brokerage.brokeragefirm.common.exception;

import com.brokerage.brokeragefirm.common.enums.Error;

public class NotFoundException extends CustomException {
    public NotFoundException(Error error, Object... params) {
        super(error, params);
    }
}
