package org.adso.minimarket.config;
@Configuration
@EnableJpaRepositories(
        basePackages = "org.adso.minimarket.repository.jpa" // Put SQL repos here
)
@EnableElasticsearchRepositories(
        basePackages = "org.adso.minimarket.repository.elastic" // Put ES repos here
)
public class RepositoryConfig {
}
