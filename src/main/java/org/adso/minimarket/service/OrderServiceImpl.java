package org.adso.minimarket.service;

import org.adso.minimarket.dto.OrderDetails;
import org.adso.minimarket.exception.BadRequestException;
import org.adso.minimarket.models.*;
import org.adso.minimarket.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
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

        for (CartItem ci : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(ci.getProduct());
            orderItem.setQuantity(ci.getQuantity());
            orderItem.setPrice(ci.getUnitPrice());
            orderItem.setSubTotal(ci.getUnitPrice()
                    .multiply(BigDecimal.valueOf(ci.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP));
            order.getOrderItems().add(orderItem);
        }

        return orderRepository.save(order);
    }

}
