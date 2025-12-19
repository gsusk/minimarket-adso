package org.adso.minimarket.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.adso.minimarket.error.BasicErrorResponse;
import org.adso.minimarket.error.DataIntegrityViolationResponse;
import org.adso.minimarket.error.ValidationErrorResponse;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.exception.WrongCredentialsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler {

    private static final Map<String, String> CONSTRAINT_MESSAGES = Map.of(
            "uk_user_email", "Email already in use"
    );

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<@NonNull DataIntegrityViolationResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        Throwable root = getRootCause(ex);
        String message = root.getMessage() != null ? root.getMessage() : "";

        DataIntegrityViolationResponse res = new DataIntegrityViolationResponse();
        String lower = message.toLowerCase();

        res.setMessage("CONSTRAINT_VIOLATION");

        for (String constraint : CONSTRAINT_MESSAGES.keySet()) {
            if (lower.contains(constraint)) {

                String field = constraint.substring(constraint.lastIndexOf("_") + 1);

                res.addError(new DataIntegrityViolationResponse.ErrorDetail(
                        CONSTRAINT_MESSAGES.get(constraint),
                        field
                ));

                return new ResponseEntity<>(res, HttpStatus.CONFLICT);
            }
        }

        res.setMessage("UNKNOWN_CONFLICT");

        return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, List<String>> groupedErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                FieldError::getDefaultMessage,
                                Collectors.toList())
                ));

        ValidationErrorResponse response = new ValidationErrorResponse();

        groupedErrors.forEach((field, messages) -> {
            String combinedMessage = String.join(" | ", messages);
            response.addError(combinedMessage, field);
        });

        response.setMessage("VALIDATION_ERROR");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<BasicErrorResponse> handleBadRequestException(
            WrongCredentialsException ex
    ) {
        BasicErrorResponse err = new BasicErrorResponse();
        err.setMessage(ex.getMessage());
        err.setCode("UNAUTHORIZED");

        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex
    ) {
        ValidationErrorResponse err = new ValidationErrorResponse();
        Map<String, String> groupedErrors = ex.getConstraintViolations().stream().collect(
                Collectors.groupingBy(
                        (violation -> {
                            return violation.getPropertyPath().toString().split("\\.")[1];
                        }),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.joining(", "))
                )
        );

        groupedErrors.forEach(err::addError);

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BasicErrorResponse> handleNotFoundException(
            NotFoundException ex
    ) {
        BasicErrorResponse err = new BasicErrorResponse();
        err.setMessage(ex.getMessage());
        err.setCode("NOT_FOUND");

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format(
                "Invalid value '%s' for parameter '%s'",
                ex.getValue(),
                ex.getName()
        );

        return ResponseEntity.badRequest()
                .body(message);
    }

    private Throwable getRootCause(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}

