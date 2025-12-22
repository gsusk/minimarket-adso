package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.dto.request.CreateProductRequest;
import org.adso.minimarket.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    ProductControllerImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @PostMapping("/product")
    public ResponseEntity<?> create(@RequestBody @Valid CreateProductRequest productRequest) {
        return ResponseEntity.created(URI.create("/product/" + productService.createProduct(productRequest))).build();
    }
}
