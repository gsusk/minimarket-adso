package org.adso.minimarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.adso.minimarket.models.document.ProductDocument;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class SearchResult {
    Integer total;
    BigDecimal minPrice;
    BigDecimal maxPrice;
    List<ProductDocument> products;
}
