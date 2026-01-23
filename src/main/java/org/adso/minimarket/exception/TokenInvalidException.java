package org.adso.minimarket.exception;

import org.springframework.http.HttpStatus;

public class TokenInvalidException extends BaseException {
    private static final String title = "UNAUTHORIZED";
    private static final HttpStatus code = HttpStatus.UNAUTHORIZED;
    private static final ErrorCode errorCode = ErrorCode.AUTH_TOKEN_INVALID;

    public TokenInvalidException(String message) {
        super(message, code, title, errorCode);
    }
}
