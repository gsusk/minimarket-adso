package org.adso.minimarket.controller.api;

import org.adso.minimarket.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class SearchControllerImpl implements SearchController {
    private static final Logger log = LoggerFactory.getLogger(SearchControllerImpl.class);
    private final SearchService searchService;

    public SearchControllerImpl(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam("q") String query,
                               @RequestParam("brand") Optional<String> brand,
                               @RequestParam("min") Optional<BigDecimal> minValue,
                               @RequestParam("max") Optional<BigDecimal> maxValue) {
        log.info("query: {}\nbrand: {}\nmin: {}\nmax: {}",query, brand, minValue, maxValue);
        return ResponseEntity.ok("");
    }
}
