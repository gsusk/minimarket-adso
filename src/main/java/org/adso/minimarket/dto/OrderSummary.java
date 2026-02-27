package org.adso.minimarket.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderSummary {
    private UUID orderId;
    private Long userId;
    private String email;
    private BigDecimal totalAmount;
    private List<OrderItemSummary> items;
    private LocalDateTime createdAt;
}
