package org.adso.minimarket.service;

import org.adso.minimarket.dto.OrderDetails;
import org.adso.minimarket.exception.BadRequestException;
import org.adso.minimarket.exception.OrderInsufficientStockException;
import org.adso.minimarket.models.cart.Cart;
import org.adso.minimarket.models.cart.CartItem;
import org.adso.minimarket.models.order.Order;
import org.adso.minimarket.models.order.OrderItem;
import org.adso.minimarket.models.order.OrderStatus;
import org.adso.minimarket.models.product.Product;
import org.adso.minimarket.models.user.User;
import org.adso.minimarket.repository.jpa.OrderRepository;
import org.adso.minimarket.repository.jpa.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            CartService cartService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }


    @Override
    @Transactional
    public OrderDetails placeOrder(User user) {
        Order order = createOrder(user);

        return new OrderDetails(order.getId(),
                order.getEmail(),
                user.getId(),
                order.getStatus().toString().toLowerCase(),
                order.getTotalAmount(),
                order.getCreatedAt());
    }

    public Order createOrder(User user) {
        Cart cart = cartService.getCart(user.getId(), null);
        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Invalid order: Cart empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setEmail(user.getEmail());

        List<Long> productIds = cart.getCartItems().stream()
                .map(ci -> ci.getProduct().getId())
                .collect(Collectors.toList());

        Map<Long, Product> productMap = productRepository.findForUpdateAllByIdIn(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cart.getCartItems()) {
            Product product = productMap.get(ci.getProduct().getId());

            if (product.getStock() < ci.getQuantity()) {
                throw new OrderInsufficientStockException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - ci.getQuantity());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(ci.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setSubTotal(product.getPrice()
                    .multiply(BigDecimal.valueOf(ci.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP));
            order.getOrderItems().add(orderItem);
            total = total.add(orderItem.getSubTotal()).setScale(2, RoundingMode.HALF_UP);
        }

        cart.getCartItems().clear();

        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }
}
