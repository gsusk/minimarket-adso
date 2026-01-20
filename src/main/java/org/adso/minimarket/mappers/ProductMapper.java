package org.adso.minimarket.mappers;

import org.adso.minimarket.dto.DetailedProduct;
import org.adso.minimarket.models.product.Image;
import org.adso.minimarket.models.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(
            target = "price",
            expression = "java(product.getPrice().toPlainString())"
    )
    @Mapping(target = "category", source = "category")
    DetailedProduct toDto(Product product);

    default String map(Image image) {
        return image == null ? null : image.getUrl();
    }
}
