package org.adso.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {
    private Set<Item> items;
    private String subTotal;
    private int size;
}
