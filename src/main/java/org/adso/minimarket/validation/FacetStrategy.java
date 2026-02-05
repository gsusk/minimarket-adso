package org.adso.minimarket.validation;

public enum FacetStrategy {
    TERMS,
    SIGNIFICANT_TERMS,
    SAMPLER,
    NONE;

    public static FacetStrategy fromString(String strategy) {
        if (strategy == null) {
            return NONE;
        }
        return switch (strategy.toUpperCase()) {
            case "TERMS" -> TERMS;
            case "SIGNIFICANT_TERMS" -> SIGNIFICANT_TERMS;
            case "SAMPLER" -> SAMPLER;
            case "NONE" -> NONE;
            default -> NONE;
        };
    }

    public boolean isFacetable() {
        return this != NONE;
    }

    public int getDefaultSize() {
        return switch (this) {
            case TERMS -> 20;
            case SIGNIFICANT_TERMS -> 10;
            case SAMPLER -> 5;
            case NONE -> 0;
        };
    }

    public int getSampleSize() {
        return this == SAMPLER ? 1000 : 0;
    }
}
