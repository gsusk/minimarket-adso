package org.adso.minimarket;

import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.User;
import org.adso.minimarket.repository.CategoryRepository;
import org.adso.minimarket.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class MinimarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinimarketApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner initDb(UserRepository userRepository, CategoryRepository categoryRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(new User("mario", "contreras", "marioc@gmail.com", "password123"));
                userRepository.save(new User("jorge", "pic", "pepe@gmail.com", "password123"));
            }

            if (categoryRepository.count() == 0) {
                categoryRepository.save(new Category("Ropa"));
                categoryRepository.save(new Category("Electrodomesticos"));
            }
        };
    }
}
