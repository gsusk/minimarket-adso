package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItem {
    @JsonProperty("product_id")
    private Long productId;
    private String name;
    @JsonProperty("unit_price")
    private String unitPrice;
    private int quantity;
}
