package org.adso.minimarket.controller.api;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.adso.minimarket.constant.ProductRoutes;
import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.DetailedProduct;
import org.adso.minimarket.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    ProductControllerImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ProductRoutes.CREATE_PRODUCT)
    public ResponseEntity<?> create(@RequestBody @Valid CreateProductRequest productRequest) {
        return ResponseEntity.created(URI.create("/products/" + productService.createProduct(productRequest))).build();
    }

    @Override
    @GetMapping(ProductRoutes.GET_PRODUCT)
    public ResponseEntity<DetailedProduct> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getDetailedProductById(id));
    }
}
