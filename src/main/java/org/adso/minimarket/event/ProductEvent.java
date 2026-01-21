package org.adso.minimarket.event;

import lombok.Getter;
import org.adso.minimarket.models.product.Product;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProductEvent extends ApplicationEvent {
    private final Product product;
    private final String categoryName;

    public ProductEvent(Object source, Product product, String categoryName) {
        super(source);
        this.product = product;
        this.categoryName = categoryName;
    }

}
