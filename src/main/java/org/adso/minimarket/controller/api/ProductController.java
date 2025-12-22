package org.adso.minimarket.controller.api;

import org.adso.minimarket.dto.request.CreateProductRequest;
import org.springframework.http.ResponseEntity;

public interface ProductController {
    ResponseEntity<?> create(CreateProductRequest productRequest);
}
