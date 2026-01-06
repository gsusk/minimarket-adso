package org.adso.minimarket.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cart_product_id", columnNames = {"product_id", "cart_id"}),
        }
)
public class CartItem {

    @EmbeddedId
    private CartItemKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_cart_item_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartId")
    @JoinColumn(name = "cart_id", foreignKey = @ForeignKey(name = "fk_cart_item_cart"))
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
        this.quantity += q;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public CartItem(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice().setScale(2, RoundingMode.HALF_UP);

        this.id = new CartItemKey(cart.getId(), product.getId());
    }
}
