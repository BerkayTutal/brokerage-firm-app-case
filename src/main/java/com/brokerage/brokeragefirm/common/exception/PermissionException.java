package com.brokerage.brokeragefirm.common.exception;

import com.brokerage.brokeragefirm.common.enums.Error;

public class PermissionException extends CustomException {
    public PermissionException(Error error, Object... params) {
        super(error, params);
    }
}
