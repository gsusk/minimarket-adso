package org.adso.minimarket.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductCard(
        Long id,
        String name,
        String category,
        BigDecimal price,
        String brand,
        LocalDateTime createdAt
) {
}

