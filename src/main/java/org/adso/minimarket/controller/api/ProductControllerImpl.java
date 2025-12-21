package org.adso.minimarket.controller.api;

import org.adso.minimarket.dto.request.ProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductControllerImpl implements ProductController {

    @Override
    @PostMapping("/product")
    public ResponseEntity<?> create(ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
