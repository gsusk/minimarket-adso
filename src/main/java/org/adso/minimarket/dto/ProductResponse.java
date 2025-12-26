package org.adso.minimarket.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String price;
    private List<String> images = new ArrayList<>();
    private CategorySummary category;
    private int stock;

    @JsonProperty(value = "listed_at")
    private LocalDateTime createdAt;
}
