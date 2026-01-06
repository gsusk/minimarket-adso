package org.adso.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {
    private List<Item> items;
    private String subTotal;
    private int size;
}
