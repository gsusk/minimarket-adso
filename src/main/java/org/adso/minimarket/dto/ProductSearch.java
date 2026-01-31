package org.adso.minimarket.dto;

import lombok.Getter;
import org.adso.minimarket.models.product.Product;

import java.util.List;
import java.util.Map;

@Getter
public class ProductSearch {
    private List<Product> products;
    private List<Map<String, Object>> facets;
    private int total;
}
