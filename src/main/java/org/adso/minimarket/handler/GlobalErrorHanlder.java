package org.adso.minimarket.handler;

import lombok.NonNull;
import org.adso.minimarket.error.ConstraintViolationResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHanlder {

    private static final Map<String, String> CONSTRAINT_MESSAGES = Map.of(
            "uk_user_email", "Email already in use"
    );

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<@NonNull ConstraintViolationResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        Throwable root = getRootCause(ex);
        String message = root.getMessage() != null ? root.getMessage() : "";

        ConstraintViolationResponse res = new ConstraintViolationResponse();

        for (String constraint : CONSTRAINT_MESSAGES.keySet()) {
            if (message.contains(constraint)) {

                res.addError(new ConstraintViolationResponse.ErrorDetail(
                                CONSTRAINT_MESSAGES.get(constraint),
                                constraint.substring(constraint.lastIndexOf("_") + 1)
                        )
                );
            }
        }

        if (res.getCode() == null) {
            res.setCode("UNKNOWN_CONFLICT");
        } else {
            res.setCode("CONSTRAINT_VIOLATION");
        }


        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }


    private Throwable getRootCause(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    private String sanitize(String msg) {
        if (msg == null) return "";
        return msg.replaceAll("[\\n\\r\\t]+", " ").trim();
    }
}

