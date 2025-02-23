package com.brokerage.brokeragefirm.common.exception;

import com.brokerage.brokeragefirm.common.enums.Error;

public class DuplicateEntryException extends CustomException {
    public DuplicateEntryException(Error error, Object... params) {
        super(error, params);
    }
}
