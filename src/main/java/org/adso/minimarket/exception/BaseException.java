package org.adso.minimarket.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final HttpStatus code;
    private final String title;

    public BaseException(String message, HttpStatus code, String title) {
        super(message);
        this.code = code;
        this.title = title;
    }

    public HttpStatus getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}