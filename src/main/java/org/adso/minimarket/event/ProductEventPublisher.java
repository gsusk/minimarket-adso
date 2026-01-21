package org.adso.minimarket.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProductCreatedEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishProductCreatedEvent()
}
