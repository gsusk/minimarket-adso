package org.adso.minimarket.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationError {
    private final String attributeName;
    private final String errorMessage;
    private final Object providedValue;
    private final String constraint;

    public ValidationError(String attributeName, String errorMessage) {
        this(attributeName, errorMessage, null, null);
    }

    public ValidationError(String attributeName, String errorMessage, Object providedValue) {
        this(attributeName, errorMessage, providedValue, null);
    }
}
