package org.adso.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@AllArgsConstructor
public class SearchFilters {
    private String category;
    private String brand;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private Map<String, String> attributes;
}
