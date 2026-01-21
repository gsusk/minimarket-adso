package org.adso.minimarket.event;

import org.adso.minimarket.models.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Async
    public void publishProductCreatedEvent(Product product, String categoryName){
        ProductEvent productEvent = new ProductEvent(this, product, categoryName);
        applicationEventPublisher.publishEvent(productEvent);
    };
}
