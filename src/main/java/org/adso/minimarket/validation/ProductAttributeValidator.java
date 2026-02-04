package org.adso.minimarket.validation;

import org.adso.minimarket.exception.AttributeValidationException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductAttributeValidator {

    public void validate(Map<String, Object> attributes, List<Map<String, Object>> definitionMaps) {
        if (definitionMaps == null || definitionMaps.isEmpty()) {
            return;
        }

        List<ValidationError> errors = new ArrayList<>();

        List<AttributeDefinition> definitions;
        try {
            definitions = definitionMaps.stream()
                    .map(AttributeDefinition::fromMap)
                    .toList();
        } catch (Exception e) {
            errors.add(new ValidationError("schema", "Invalid attribute schema: " + e.getMessage()));
            throw new AttributeValidationException(errors);
        }

        Set<String> definedAttributeNames = new HashSet<>();
        for (AttributeDefinition def : definitions) {
            definedAttributeNames.add(def.getName());
        }

        for (AttributeDefinition def : definitions) {
            validateAttribute(def, attributes, errors);
        }

        if (attributes != null) {
            for (String attrName : attributes.keySet()) {
                if (!definedAttributeNames.contains(attrName)) {
                    errors.add(new ValidationError(
                            attrName,
                            "Unknown attribute '" + attrName + "' is not defined in the category schema",
                            attributes.get(attrName)
                    ));
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new AttributeValidationException(errors);
        }
    }

    private void validateAttribute(AttributeDefinition def, Map<String, Object> attributes, List<ValidationError> errors) {
        String name = def.getName();
        Object value = attributes != null ? attributes.get(name) : null;

        if (def.isRequired()) {
            if (attributes == null || !attributes.containsKey(name)) {
                errors.add(new ValidationError(name, "Missing required attribute: " + name));
                return;
            }
            if (value == null) {
                errors.add(new ValidationError(name, "Attribute '" + name + "' cannot be null", value));
                return;
            }
        }

        if (value == null || (attributes != null && !attributes.containsKey(name))) {
            return;
        }

        validateType(def, value, errors);

        if (def.hasOptions()) {
            validateEnum(def, value, errors);
        }

        if (def.getType() == AttributeType.NUMBER) {
            validateRange(def, value, errors);
        }
    }

    private void validateType(AttributeDefinition def, Object value, List<ValidationError> errors) {
        String name = def.getName();
        AttributeType expectedType = def.getType();

        boolean isValid = switch (expectedType) {
            case STRING -> value instanceof String && !((String) value).isBlank();
            case NUMBER -> value instanceof Number;
            case BOOLEAN -> value instanceof Boolean;
        };

        if (!isValid) {
            String actualType = getActualType(value);
            String expectedTypeStr = expectedType.name().toLowerCase();
            
            if (expectedType == AttributeType.STRING && value instanceof String && ((String) value).isBlank()) {
                errors.add(new ValidationError(
                        name,
                        "Attribute '" + name + "' cannot be blank",
                        value
                ));
            } else {
                errors.add(new ValidationError(
                        name,
                        "Attribute '" + name + "' must be a " + expectedTypeStr + ", but got " + actualType,
                        value
                ));
            }
        }
    }

    private void validateEnum(AttributeDefinition def, Object value, List<ValidationError> errors) {
        String name = def.getName();
        List<?> options = def.getOptions();

        boolean found = false;

        if (value instanceof Number) {
            double valueDouble = ((Number) value).doubleValue();
            found = options.stream()
                    .filter(opt -> opt instanceof Number)
                    .map(opt -> ((Number) opt).doubleValue())
                    .anyMatch(optVal -> Double.compare(optVal, valueDouble) == 0);
        } else {
            found = options.contains(value);
        }

        if (!found) {
            errors.add(new ValidationError(
                    name,
                    "Attribute '" + name + "' has invalid value. Allowed values: " + options,
                    value,
                    "enum: " + options
            ));
        }
    }

    private void validateRange(AttributeDefinition def, Object value, List<ValidationError> errors) {
        if (!(value instanceof Number)) {
            return;
        }

        String name = def.getName();
        double doubleVal = ((Number) value).doubleValue();

        if (def.hasMin()) {
            double min = def.getMin().doubleValue();
            if (doubleVal < min) {
                errors.add(new ValidationError(
                        name,
                        "Attribute '" + name + "' must be >= " + min + ", but got " + doubleVal,
                        value,
                        "min: " + min
                ));
            }
        }

        if (def.hasMax()) {
            double max = def.getMax().doubleValue();
            if (doubleVal > max) {
                errors.add(new ValidationError(
                        name,
                        "Attribute '" + name + "' must be <= " + max + ", but got " + doubleVal,
                        value,
                        "max: " + max
                ));
            }
        }
    }

    private String getActualType(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return ((String) value).isBlank() ? "blank string" : "string";
        }
        if (value instanceof Number) {
            return "number";
        }
        if (value instanceof Boolean) {
            return "boolean";
        }
        return value.getClass().getSimpleName();
    }
}
