package org.adso.minimarket.event;

import lombok.Getter;
import org.adso.minimarket.models.product.Product;

@Getter
public class ProductCreatedEvent {
    private final Product product;
    private final String categoryName;

    public ProductCreatedEvent(Product product, String categoryName) {
        this.product = product;
        this.categoryName = categoryName;
    }

    public void
}
