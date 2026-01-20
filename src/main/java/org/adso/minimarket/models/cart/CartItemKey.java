package org.adso.minimarket.models.cart;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartItemKey implements Serializable {
    @Column(name = "cart_id")
    Long cartId;

    @Column(name = "product_id")
    Long productId;

    public CartItemKey(Long cartId, Long productId) {
        this.cartId = cartId;
        this.productId = productId;
    }

    public CartItemKey() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CartItemKey that = (CartItemKey) o;
        return Objects.equals(cartId, that.cartId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, productId);
    }
}
