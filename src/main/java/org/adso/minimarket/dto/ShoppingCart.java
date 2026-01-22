package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {
    private Set<ShoppingCartItem> shoppingCartItems;
    @JsonProperty("sub_total")
    private String subTotal;
    private int size;
}
