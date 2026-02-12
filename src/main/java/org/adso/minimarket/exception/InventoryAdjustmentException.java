package org.adso.minimarket.exception;

import org.springframework.http.HttpStatus;

public class InventoryAdjustmentException extends BaseException {
    private static final String title = "INVENTORY ADJUSTMENT FAILED";
    private static final HttpStatus code = HttpStatus.BAD_REQUEST;
    private static final ErrorCode errorCode = ErrorCode.BUSINESS_INVENTORY_INVALID_ADJUSTMENT;

    public InventoryAdjustmentException(String message) {
        super(message, code, title, errorCode);
    }
}
