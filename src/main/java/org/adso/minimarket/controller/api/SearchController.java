package org.adso.minimarket.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface SearchController {
     ResponseEntity<?> searchProducts(Map<String, String> allParams);
}
