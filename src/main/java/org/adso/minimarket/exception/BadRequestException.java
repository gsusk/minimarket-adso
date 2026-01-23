package org.adso.minimarket.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    private static final String title = "BAD REQUEST";
    private static final HttpStatus code = HttpStatus.BAD_REQUEST;
    private static final ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

    public BadRequestException(String message) {
        super(message, code, title, errorCode);
    }

    public BadRequestException(String message, ErrorCode errorCode) {
        super(message, code, title, errorCode);
    }
}
