package org.adso.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FacetValue {
    private String value;
    private long count;
}
