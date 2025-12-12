package org.adso.minimarket.error;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorResponse {
    @Setter
    private String message;
    private final List<FieldsError> errors = new ArrayList<>();

    public List<FieldsError> getErrors() {
        return List.copyOf(errors);
    }

    public void addError(String message, String field){
        this.errors.add(
                new FieldsError(
                        field,
                        message
                )
        );
    }

    public static class FieldsError {
        private String field;
        private String message;

        public FieldsError(String field, String message) {
            this.field = field;
            this.message = message;
        }
  }
}