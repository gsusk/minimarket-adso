package org.adso.minimarket.event;

import lombok.Getter;
import org.adso.minimarket.models.product.Product;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProductCreatedEvent extends ApplicationEvent {
    private final Product product;
    private final String categoryName;

    public ProductCreatedEvent(Object source, Product product, String categoryName) {
        super(source);
        this.product = product;
        this.categoryName = categoryName;
    }

}
