package org.adso.minimarket.service;

import org.adso.minimarket.models.Category;

public interface CategoryService {
    Category getById(Long id);

    Category createCategory(org.adso.minimarket.dto.CreateCategoryRequest request);
}
