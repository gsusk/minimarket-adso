package org.adso.minimarket.exception;

import org.springframework.http.HttpStatus;

public class InternalErrorException extends BaseException {
    private static final String title = "INTERNAL SERVER ERROR";
    private static final HttpStatus code = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final ErrorCode errorCode = ErrorCode.SERVER_INTERNAL_ERROR;

    public InternalErrorException(String message) {
        super(message, code, title, errorCode);
    }
}
