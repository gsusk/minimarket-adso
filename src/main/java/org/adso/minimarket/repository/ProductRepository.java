package org.adso.minimarket.repository;

import org.adso.minimarket.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    <S extends Product> S save(S entity);
}
