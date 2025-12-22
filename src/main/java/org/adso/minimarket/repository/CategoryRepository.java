package org.adso.minimarket.repository;

import org.adso.minimarket.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Override
    Category getReferenceById(Long id);
}
