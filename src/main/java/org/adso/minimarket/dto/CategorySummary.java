package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategorySummary {
    private Long id;

    @JsonProperty("category_name")
    private String name;
}
