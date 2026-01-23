package org.adso.minimarket.exception;

import org.springframework.http.HttpStatus;

public class WrongCredentialsException extends BaseException {
    private static final String title = "WRONG CREDENTIALS";
    private static final HttpStatus code = HttpStatus.UNAUTHORIZED;
    private static final ErrorCode errorCode = ErrorCode.AUTH_INVALID_CREDENTIALS;

    public WrongCredentialsException(String message) {
        super(message, code, title, errorCode);
    }
}
