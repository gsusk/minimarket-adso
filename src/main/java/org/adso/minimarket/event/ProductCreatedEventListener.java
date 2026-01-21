package org.adso.minimarket.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProductCreatedEventListener {
    @Async
    @EventListener
    public void onApplicationEvent(ProductCreatedEvent event) {
        System.out.println("evento recibido" + event.getProduct());
    }
}
