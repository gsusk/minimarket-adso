package org.adso.minimarket.exception;

public class BadAuthCredentialsException extends RuntimeException {
    public BadAuthCredentialsException(String message) {
        super(message);
    }
}
