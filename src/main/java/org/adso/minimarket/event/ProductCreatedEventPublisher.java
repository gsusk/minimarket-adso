package org.adso.minimarket.event;

import org.adso.minimarket.models.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProductCreatedEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public ProductCreatedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Async
    public void publishProductCreatedEvent(Product product, String categoryName) {
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(this, product, categoryName);
        applicationEventPublisher.publishEvent(productCreatedEvent);
    }
}
