package org.adso.minimarket.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal price;

    @Min(value = 0, message = "stock cant be less than 0")
    private Integer stock;

    private List<String> images_url = new ArrayList<>();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_product_category"), nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<CartItem> cartItems = new HashSet<>();

    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedAt;

    public Product(Long id, String name, String description, BigDecimal price, Integer stock, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = normalizePrice(price);
        this.category = category;
    }

    public Product(String name, String description, BigDecimal price, Category category) {
        this.name = name;
        this.description = description;
        this.price = normalizePrice(price);
        this.category = category;
    }

    BigDecimal normalizePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (price.signum() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(description,
                product.description) && Objects.equals(price, product.price) && Objects.equals(images_url,
                product.images_url) && Objects.equals(category, product.category) && Objects.equals(cartItems,
                product.cartItems) && Objects.equals(createdAt, product.createdAt) && Objects.equals(updatedAt,
                product.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, images_url, category, cartItems, createdAt, updatedAt);
    }
}
