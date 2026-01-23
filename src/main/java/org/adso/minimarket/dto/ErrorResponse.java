package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String message,
        String errorCode,
        LocalDateTime timestamp,
        String path,
        List<ValidationError> validationErrors
) {
    public record ValidationError(
            String field,
            String message,
            Object rejectedValue
    ) {
    }
}
