package org.adso.minimarket.service;

import org.adso.minimarket.dto.OrderDetails;
import org.adso.minimarket.exception.BadRequestException;
import org.adso.minimarket.exception.OrderInsufficientStockException;
import org.adso.minimarket.models.*;
import org.adso.minimarket.repository.OrderRepository;
import org.adso.minimarket.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
    }


    @Override
    @Transactional
    public OrderDetails placeOrder(User user) {
        Cart cart = cartService.getCart(user.getId(), null);
        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Invalid order: Cart empty");
        }

        Order order = createOrder(user, cart);

        cart.setStatus(CartStatus.COMPLETE);

        return new OrderDetails(order.getId(), order.getEmail(), user.getId(),
                order.getStatus().toString().toLowerCase(),
                order.getCreatedAt());
    }

    /**
     * PROBAR CANTIDADES SEGUN UNIDADES DISPONIBLES Y EDICION DEL CARRITO SOBRE LA MARCHA
     */
    public Order createOrder(User user, Cart cart) {
        Order order = new Order();
        order.setUser(user);
        order.setEmail(user.getEmail());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cart.getCartItems()) {

            if (ci.getQuantity() > ci.getProduct().getStock()) {
                throw new OrderInsufficientStockException(String.format("Not enough stock for product: \"%s",
                        ci.getProduct().getName()));
            }

            ci.getProduct().setStock(ci.getProduct().getStock() - ci.getQuantity());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(ci.getProduct());
            orderItem.setQuantity(ci.getQuantity());
            orderItem.setPrice(ci.getUnitPrice());
            orderItem.setSubTotal(ci.getUnitPrice()
                    .multiply(BigDecimal.valueOf(ci.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP));
            order.getOrderItems().add(orderItem);
            total = total.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }

        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

}
