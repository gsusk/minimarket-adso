package org.adso.minimarket;

import org.adso.minimarket.models.*;
import org.adso.minimarket.repository.CartRepository;
import org.adso.minimarket.repository.CategoryRepository;
import org.adso.minimarket.repository.ProductRepository;
import org.adso.minimarket.repository.UserRepository;
import org.adso.minimarket.service.CartService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootApplication
public class MinimarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinimarketApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner seedDb(UserRepository userRepository, CategoryRepository categoryRepository,
                                    CartRepository cartRepository, CartService cartService,
                                    ProductRepository productRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                var catRopa = categoryRepository.save(new Category("Ropa"));
                var catElectro = categoryRepository.save(new Category("Electrodomesticos"));
                var furnit = categoryRepository.save(new Category("Muebles"));

                var pCamiseta = productRepository.save(new Product("Camiseta blanca", "Grande y cómoda",
                        new BigDecimal("2000"), 2, catRopa));
                var pMesa = productRepository.save(new Product("Mesa grande", "2 metros de madera", new BigDecimal(
                        "1000"), 3, furnit));

                productRepository.save(new Product("Pc gamer", "con luz led y ventana", new BigDecimal(
                        "1025"), 3, catElectro));
                productRepository.save(new Product("smartphone", "phone with nice camera", new BigDecimal(
                        "1025"), 3, catElectro));

                User mario = userRepository.save(new User("mario", "contreras", "marioc@gmail.com", "password123"));

                Cart marioCart = new Cart(mario);
                CartItem itemMario = new CartItem(marioCart, pCamiseta, 2);
                marioCart.getCartItems().add(itemMario);
                cartRepository.save(marioCart);

                UUID gid = UUID.randomUUID();
                Cart guestCart = new Cart(gid);

                CartItem guestItem1 = new CartItem(guestCart, pCamiseta, 5);
                CartItem guestItem2 = new CartItem(guestCart, pMesa, 1);

                guestCart.getCartItems().add(guestItem1);
                guestCart.getCartItems().add(guestItem2);
                cartRepository.save(guestCart);

//                Thread.sleep(6000);
//                cartService.mergeCarts(mario.getId(), gid);

            }
        };
    }
}
