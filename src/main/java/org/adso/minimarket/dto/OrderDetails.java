package org.adso.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
    private UUID id;
    private String email;
    private Long userId;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
}
