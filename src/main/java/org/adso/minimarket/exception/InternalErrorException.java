package org.adso.minimarket.exception;

public class InternalErrorException extends RuntimeException {
    InternalErrorException(String message) {
        super(message);
    }
}
