package com.brokerage.brokeragefirm.common.exception;

import com.brokerage.brokeragefirm.common.enums.Error;

public class OperationNotAllowedException extends CustomException {
    public OperationNotAllowedException(Error error, Object... params) {
        super(error, params);
    }
}
