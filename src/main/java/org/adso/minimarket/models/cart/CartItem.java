package org.adso.minimarket.models.cart;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.adso.minimarket.exception.InternalErrorException;
import org.adso.minimarket.models.product.Product;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @EmbeddedId
    private CartItemKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(nullable = false)
    private int quantity;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal unitPrice;

    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedAt;

    public Product getProduct() {
        return this.product;
    }

    public CartItemKey getId() {
        return this.id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public void addToQuantity(int q) {
        if (quantity < 0) {
            throw new InternalErrorException("Invalid quantity. Cannot be negative");
        }
        this.quantity += q;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setQuantity(Integer quantity) {
        if (quantity < 0) {
            throw new InternalErrorException("Invalid quantity. Cannot be negative");
        }
        this.quantity = quantity;
    }

    public CartItem(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice().setScale(2, RoundingMode.HALF_UP);

        this.id = new CartItemKey(cart.getId(), product.getId());
    }
}
