package org.adso.minimarket.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    private static final String title = "NOT FOUND";
    private static final HttpStatus code = HttpStatus.NOT_FOUND;
    private static final ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

    public NotFoundException(String message) {
        super(message, code, title, errorCode);
    }
}
