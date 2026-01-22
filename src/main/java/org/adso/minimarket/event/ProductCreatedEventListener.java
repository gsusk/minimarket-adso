package org.adso.minimarket.event;

import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.models.product.Product;
import org.adso.minimarket.service.SearchService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProductCreatedEventListener {
    private final SearchService searchService;

    public ProductCreatedEventListener(SearchService searchService) {
        this.searchService = searchService;
    }

    @Async
    @EventListener
    public void onApplicationEvent(ProductCreatedEvent event) {
        Product p = event.getProduct();
        ProductDocument productDocument = new ProductDocument(
                p.getId(),
                p.getName(),
                p.getDescription(),
                event.getCategoryName(),
                p.getPrice(),
                p.getBrand(),
                p.getStock(),
                p.getAttributes(),
                p.getCreatedAt()
        );
        searchService.saveIndex(productDocument);
    }
}
