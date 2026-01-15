package org.adso.minimarket.exception;

public class CartBusinessException extends RuntimeException {
    public CartBusinessException(String message) {
        super(message);
    }
}
