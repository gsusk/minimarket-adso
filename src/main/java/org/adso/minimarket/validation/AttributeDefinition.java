package org.adso.minimarket.validation;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class AttributeDefinition {
    private final String name;
    private final AttributeType type;
    private final boolean required;
    private final boolean facetable;
    private final List<?> options;
    private final Number min;
    private final Number max;

    public AttributeDefinition(String name, AttributeType type, boolean required, boolean facetable,
                               List<?> options, Number min, Number max) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.facetable = facetable;
        this.options = options;
        this.min = min;
        this.max = max;
    }

    public static AttributeDefinition fromMap(Map<String, Object> map) {
        String name = (String) map.get("name");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Attribute definition must have a name");
        }

        String typeStr = (String) map.get("type");
        if (typeStr == null || typeStr.isBlank()) {
            throw new IllegalArgumentException("Attribute definition must have a type");
        }
        AttributeType type = AttributeType.fromString(typeStr);

        boolean required = map.containsKey("required") ? (Boolean) map.get("required") : false;
        boolean facetable = map.containsKey("facetable") ? (Boolean) map.get("facetable") : false;

        List<?> options = map.containsKey("options") ? (List<?>) map.get("options") : null;

        Number min = map.containsKey("min") ? (Number) map.get("min") : null;
        Number max = map.containsKey("max") ? (Number) map.get("max") : null;

        return new AttributeDefinition(name, type, required, facetable, options, min, max);
    }

    public boolean hasOptions() {
        return options != null && !options.isEmpty();
    }

    public boolean hasMin() {
        return min != null;
    }

    public boolean hasMax() {
        return max != null;
    }
}
