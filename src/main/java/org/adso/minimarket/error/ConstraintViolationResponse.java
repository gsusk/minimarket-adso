package org.adso.minimarket.error;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class ConstraintViolationResponse {
    @Setter
    private String code;
    private List<ErrorDetail> errors = new ArrayList<>();

    public void addError(ErrorDetail error) { this.errors.add(error); }

    @Getter
    public static class ErrorDetail {
        private String message;
        private String field;

        public ErrorDetail(String message, String field) {
            this.message = message;
            this.field = field;
        }

    }
}
