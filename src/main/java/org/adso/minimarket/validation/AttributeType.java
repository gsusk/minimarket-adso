package org.adso.minimarket.validation;

public enum AttributeType {
    STRING,
    NUMBER,
    BOOLEAN,
    ENUM;

    public static AttributeType fromString(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Attribute type cannot be null");
        }
        return switch (type.toLowerCase()) {
            case "string" -> STRING;
            case "number" -> NUMBER;
            case "boolean" -> BOOLEAN;
            case "enum" -> ENUM;
            default -> throw new IllegalArgumentException("Unknown attribute type: " + type);
        };
    }
}
