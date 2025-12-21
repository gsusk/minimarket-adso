package org.adso.minimarket.controller.api;

import org.adso.minimarket.dto.request.ProductRequest;
import org.springframework.http.ResponseEntity;

public interface ProductController {
    ResponseEntity<?> create(ProductRequest productRequest);
}
