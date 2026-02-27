package org.adso.minimarket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "org.adso.minimarket.repository.jpa"
)
public class RepositoryConfig {
}
