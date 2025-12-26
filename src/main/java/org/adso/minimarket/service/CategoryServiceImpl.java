package org.adso.minimarket.service;

import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getInternalById(Long id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }
}
