package org.adso.minimarket.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final String title;
    private final ErrorCode errorCode;

    public BaseException(String message, HttpStatus status, String title, ErrorCode errorCode) {
        super(message);
        this.status = status;
        this.title = title;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}