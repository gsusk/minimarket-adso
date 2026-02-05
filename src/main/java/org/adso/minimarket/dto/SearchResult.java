package org.adso.minimarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.adso.minimarket.models.document.ProductDocument;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class SearchResult {
    Long total;
    BigDecimal minPrice;
    BigDecimal maxPrice;
    Map<String, List<FacetValue>> facets;
    List<ProductDocument> products;
}
