package org.adso.minimarket.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SearchFilters {
    private String category;
    private String brand;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
}
