package org.adso.minimarket.mappers;

import org.adso.minimarket.dto.ShoppingCart;
import org.adso.minimarket.models.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    ShoppingCart toDto(Cart cart);
}
