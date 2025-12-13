package org.adso.minimarket.handler;

import lombok.NonNull;
import org.adso.minimarket.error.ConstraintViolationResponse;
import org.adso.minimarket.error.ValidationErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    private static final Map<String, String> CONSTRAINT_MESSAGES = Map.of(
            "uk_user_email", "Email already in use"
    );

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<@NonNull ConstraintViolationResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        Throwable root = getRootCause(ex);
        String message = root.getMessage() != null ? root.getMessage() : "";

        ConstraintViolationResponse res = new ConstraintViolationResponse();
        String lower = message.toLowerCase();

        res.setCode("CONSTRAINT_VIOLATION");

        for (String constraint : CONSTRAINT_MESSAGES.keySet()) {
            if (lower.contains(constraint)) {

                String field = constraint.substring(constraint.lastIndexOf("_") + 1);

                res.addError(new ConstraintViolationResponse.ErrorDetail(
                        CONSTRAINT_MESSAGES.get(constraint),
                        field
                ));

                return new ResponseEntity<>(res, HttpStatus.CONFLICT);
            }
        }

        res.setCode("UNKNOWN_CONFLICT");

        return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> fe = ex.getBindingResult().getFieldErrors();
        ValidationErrorResponse res = new ValidationErrorResponse();

        for (FieldError field : fe) {
            res.addError(field.getDefaultMessage(), field.getField());
        }

        res.setMessage("VALIDATION-ERROR");

        System.out.println(ex.getFieldErrors());
        return new ResponseEntity<@NonNull ValidationErrorResponse>(res, HttpStatus.BAD_REQUEST);
    }

    private Throwable getRootCause(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}

