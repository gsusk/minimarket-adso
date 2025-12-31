package org.adso.minimarket.repository;

import org.adso.minimarket.models.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    <S extends Product> S save(S entity);

    boolean existsById(Long id);

    @EntityGraph(attributePaths = {"category", "images"})
    Optional<Product> findDetailedById(Long id);
}
