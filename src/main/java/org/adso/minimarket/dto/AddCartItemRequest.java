package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemRequest {
    @JsonProperty("product_id")
    @NotNull(message = "required")
    private Long productId;
    @Min(1)
    private int quantity;
}
