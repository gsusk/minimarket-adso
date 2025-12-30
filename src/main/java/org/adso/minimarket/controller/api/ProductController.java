package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.adso.minimarket.dto.CreateProductRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductController {
    ResponseEntity<?> create(@Valid CreateProductRequest productRequest);
    ResponseEntity<?> getById(@PathVariable @Min(1) @Valid Long id);
}
