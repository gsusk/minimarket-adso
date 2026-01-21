package org.adso.minimarket.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener implements ApplicationListener<ProductEvent> {
    @Override
    public void onApplicationEvent(ProductEvent event) {
        System.out.println("evento recibido" + event.getProduct()) ;
    }
}
