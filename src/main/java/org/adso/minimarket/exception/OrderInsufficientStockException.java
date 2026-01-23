package org.adso.minimarket.exception;

import org.springframework.http.HttpStatus;

public class OrderInsufficientStockException extends BaseException {
    private static final String title = "INSUFFICIENT STOCK";
    private static final HttpStatus code = HttpStatus.CONFLICT;
    private static final ErrorCode errorCode = ErrorCode.BUSINESS_INSUFFICIENT_STOCK;

    public OrderInsufficientStockException(String message) {
        super(message, code, title, errorCode);
    }
}
