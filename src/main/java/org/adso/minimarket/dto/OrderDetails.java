package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("order_id")
    private UUID id;
    private String email;
    @JsonProperty("user_id")
    private Long userId;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
}
