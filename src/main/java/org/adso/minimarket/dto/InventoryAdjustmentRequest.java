package org.adso.minimarket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.adso.minimarket.models.inventory.TransactionType;

@Data
public class InventoryAdjustmentRequest {
    @NotNull
    private Integer quantity;

    @NotNull
    private TransactionType type;

    private String reason;
}
