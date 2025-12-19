package org.adso.minimarket.error;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class DataIntegrityViolationResponse {
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
