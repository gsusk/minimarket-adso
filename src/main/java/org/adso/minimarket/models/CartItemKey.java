package org.adso.minimarket.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CartItemKey implements Serializable {
    @Column(name = "cart_id")
    Long cartId;

    @Column(name = "product_id")
    Long productId;
}
