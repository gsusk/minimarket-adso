package org.adso.minimarket.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.adso.minimarket.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    private static final Map<String, String> CONSTRAINT_MESSAGES = Map.of(
            "uk_user_email", "Email already in use"
    );

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException ex
    ) {
        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Constraint violation");

        String message = Optional.ofNullable(getRootCause(ex))
                .map(Throwable::getMessage)
                .orElse("")
                .toLowerCase();

        CONSTRAINT_MESSAGES.forEach((constraint, detail) -> {
            if (message.contains(constraint)) {
                problem.setProperty(
                        "errors",
                        List.of(Map.of(
                                "field", constraint.substring(constraint.lastIndexOf('_') + 1),
                                "detail", detail
                        ))
                );
            }
        });

        if (problem.getProperties() == null || !problem.getProperties().containsKey("errors")) {
            problem.setDetail("Unknown conflict");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle("Validation failed");
        problem.setDetail("One or more constraints were violated");

        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                FieldError::getDefaultMessage,
                                Collectors.toList()
                        )
                ))
                .entrySet()
                .stream()
                .map(entry -> Map.of(
                        "field", entry.getKey(),
                        "detail", String.join(" | ", entry.getValue())
                ))
                .toList();

        problem.setProperty("errors", errors);

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<Object> handleUnauthenticatedException(
            TokenInvalidException ex,
            WebRequest webRequest
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problem.setTitle("Unauthorized");
        problem.setInstance(URI.create(webRequest.getContextPath()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<Object> handleWrongCredentialsException(
            WrongCredentialsException ex
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problem.setTitle("unauthorized");
        return ResponseEntity.status(problem.getStatus()).body(problem);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBusinessExceptions(
            BaseException ex
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getCode(), ex.getMessage());
        problem.setTitle("Bad Request");
        return ResponseEntity.status(problem.getStatus()).body(problem);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex
    ) {
        var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation failed");

        problem.setProperty(
                "errors",
                ex.getConstraintViolations()
                        .stream()
                        .collect(Collectors.groupingBy(
                                v -> extractFieldName(v.getPropertyPath()),
                                Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                        ))
                        .entrySet()
                        .stream()
                        .map(e -> Map.of(
                                "field", e.getKey(),
                                "detail", String.join(" | ", e.getValue())
                        ))
                        .toList()
        );

        return ResponseEntity.badRequest().body(problem);
    }

    private static String extractFieldName(Path propertyPath) {
        String field = null;
        for (Path.Node node : propertyPath) {
            field = node.getName();
        }
        return field != null ? field : propertyPath.toString();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(
            NotFoundException ex
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource Not Found");
        return ResponseEntity.status(problem.getStatus()).body(problem);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setDetail(
                String.format(
                        "Invalid value '%s' for parameter '%s'",
                        ex.getValue(),
                        ex.getName()
                )
        );
        return ResponseEntity.badRequest().body(problem);
    }

    private Throwable getRootCause(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}

