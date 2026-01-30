package org.adso.minimarket.event;

import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.models.product.Product;
import org.adso.minimarket.repository.elastic.ProductSearchRepository;
import org.adso.minimarket.repository.jpa.ProductRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductSeederEvent {

    private final ProductRepository dbRepo;
    private final ProductSearchRepository esRepo;

    public ProductSeederEvent(ProductRepository dbRepo, ProductSearchRepository esRepo) {
        this.dbRepo = dbRepo;
        this.esRepo = esRepo;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void sync() {
        esRepo.deleteAll();

        var products = dbRepo.findAll();
        List<ProductDocument> productDocuments = new ArrayList<>();

        for (Product p: products) {
            productDocuments.add(new ProductDocument(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    p.getCategory().getName(),
                    p.getPrice(),
                    p.getBrand(),
                    p.getStock(),
                    p.getAttributes(),
                    p.getCreatedAt()
            ));
        }

        esRepo.saveAll(productDocuments);

        System.out.println("Elasticsearch reseteado y sincronizado.");
    }
}

