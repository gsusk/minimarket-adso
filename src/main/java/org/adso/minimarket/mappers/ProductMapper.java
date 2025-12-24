package org.adso.minimarket.mappers;

import org.adso.minimarket.dto.ProductResponse;
import org.adso.minimarket.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(
            target = "price",
            expression = "java(product.getPrice().toPlainString())"
    )
    @Mapping(target = "category", source = "category")
    ProductResponse toDto(Product product);
}
