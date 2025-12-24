package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.ProductResponse;
import org.adso.minimarket.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    ProductControllerImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @PostMapping("/products")
    public ResponseEntity<?> create(@RequestBody @Valid CreateProductRequest productRequest) {
        return ResponseEntity.created(URI.create("/products/" + productService.createProduct(productRequest))).build();
    }

    @Override
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}
