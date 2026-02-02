package org.adso.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SearchFilters {
    private String category;
    private String brand;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
}
