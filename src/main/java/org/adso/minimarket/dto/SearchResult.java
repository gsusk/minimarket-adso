package org.adso.minimarket.dto;

import co.elastic.clients.elasticsearch._types.FieldValue;
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
    List<?> brands;
    Map<String, List<String>> facets;
    List<ProductDocument> products;
}
