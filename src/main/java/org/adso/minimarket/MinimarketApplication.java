package org.adso.minimarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO:
 *  - Seguimiento de inventario
 *  - Lockout carrito
 *  - Validacion de JSON para variantes del producto
 *
 */
@SpringBootApplication
public class MinimarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinimarketApplication.class, args);
    }

//    @Bean
//    @Profile("!test")
//    public CommandLineRunner seedDb(UserRepository userRepository, CategoryRepository categoryRepository,
//                                    CartRepository cartRepository, CartService cartService,
//                                    ProductRepository productRepository) {
//        return args -> {
//        };
//    }
}

