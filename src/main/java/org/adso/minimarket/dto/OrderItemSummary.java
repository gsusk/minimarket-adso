package org.adso.minimarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class OrderItemSummary {
    private UUID id;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
