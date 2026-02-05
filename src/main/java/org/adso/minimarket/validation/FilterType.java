package org.adso.minimarket.validation;

public enum FilterType {
    TERM,
    RANGE,
    BOOLEAN,
    MULTI_SELECT,
    NONE;

    public static FilterType fromString(String type) {
        if (type == null) {
            return NONE;
        }
        return switch (type.toUpperCase()) {
            case "TERM" -> TERM;
            case "RANGE" -> RANGE;
            case "BOOLEAN" -> BOOLEAN;
            case "MULTI_SELECT" -> MULTI_SELECT;
            case "NONE" -> NONE;
            default -> NONE;
        };
    }

    public boolean isFilterable() {
        return this != NONE;
    }
}
