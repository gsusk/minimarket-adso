package org.adso.minimarket.mappers;

import org.adso.minimarket.dto.ShoppingCartItem;
import org.adso.minimarket.dto.ShoppingCart;
import org.adso.minimarket.models.cart.Cart;
import org.adso.minimarket.models.cart.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "subTotal", expression = "java(cart.getTotal().toString())")
    @Mapping(target = "size", expression = "java(cart.getSize())")
    ShoppingCart toDto(Cart cart);

    @Mapping(target = "productId", expression = "java(item.getProduct().getId())")
    @Mapping(target = "name", expression = "java(item.getProduct().getName())")
    ShoppingCartItem toCartItemDto(CartItem item);
}
