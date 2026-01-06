package org.adso.minimarket.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(check = @CheckConstraint(constraint = "user_id IS NULL AND guest_id IS NOT NULL" +
        " OR user_id IS NOT NULL AND guest_id IS NULL"))
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CartStatus status = CartStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_cart_user"))
    private User user;

    private UUID guestId;

    @Getter
    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //usuario
    public Cart(User user) {
        this.user = user;
        this.guestId = null;
        this.cartItems = new HashSet<>();
    }

    //invitado
    public Cart(UUID guestId) {
        this.user = null;
        this.guestId = guestId;
        this.cartItems = new HashSet<>();
    }

    public void setStatus(CartStatus cartStatus) {
        this.status = cartStatus;
    }

    public Long getId() {
        return this.id;
    }

    public CartStatus getStatus() {
        return status;
    }
}
