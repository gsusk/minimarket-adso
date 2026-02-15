package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.dto.CategorySummary;
import org.adso.minimarket.dto.CreateCategoryRequest;
import org.adso.minimarket.mappers.CategoryMapper;
import org.adso.minimarket.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public AdminCategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @PostMapping
    public ResponseEntity<CategorySummary> createCategory(@RequestBody @Valid CreateCategoryRequest request) {
        return new ResponseEntity<>(
                categoryMapper.toSummary(categoryService.createCategory(request)),
                HttpStatus.CREATED
        );
    }
}
