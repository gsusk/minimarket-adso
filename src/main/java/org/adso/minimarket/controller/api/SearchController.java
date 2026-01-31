package org.adso.minimarket.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Optional;

public interface SearchController {
     ResponseEntity<?> searchProducts(String query,
                                      Optional<String> brand,
                                      Optional<BigDecimal> minValue,
                                      Optional<BigDecimal> maxValue);
}
