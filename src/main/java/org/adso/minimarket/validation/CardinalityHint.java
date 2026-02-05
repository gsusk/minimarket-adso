package org.adso.minimarket.validation;

public enum CardinalityHint {
    LOW,
    MEDIUM,
    HIGH,
    UNKNOWN;

    public static CardinalityHint fromString(String hint) {
        if (hint == null) {
            return UNKNOWN;
        }
        return switch (hint.toUpperCase()) {
            case "LOW" -> LOW;
            case "MEDIUM" -> MEDIUM;
            case "HIGH" -> HIGH;
            default -> UNKNOWN;
        };
    }

    public FacetStrategy suggestFacetStrategy() {
        return switch (this) {
            case LOW -> FacetStrategy.TERMS;
            case MEDIUM -> FacetStrategy.SIGNIFICANT_TERMS;
            case HIGH -> FacetStrategy.SAMPLER;
            case UNKNOWN -> FacetStrategy.TERMS; // default??
        };
    }
}
