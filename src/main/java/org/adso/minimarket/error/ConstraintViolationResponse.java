package org.adso.minimarket.error;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static tools.jackson.databind.type.LogicalType.Collection;

@Getter
public class ConstraintViolationResponse {
    @Setter
    private String message;

    private final List<ErrorDetail> errors = new ArrayList<>();

    public void addError(ErrorDetail error) { this.errors.add(error); }

    public List<ErrorDetail> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public record ErrorDetail(String message, String field) {
    }
}
