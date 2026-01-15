package org.adso.minimarket.service;

import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.Product;
import org.adso.minimarket.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
//    @InjectMocks
//    private CategoryServiceImpl categoryService;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Test
//    void getCategoryById_Success() {
//        Category category = new Category(1L, "Ropa");
//        ReflectionTestUtils.setField(category, "products", List.of(new Product()));
//
//        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
//
//        Category result = categoryService.getInternalById(category.getId());
//
//        assertEquals(1L, result.getId());
//        assertEquals("Ropa", result.getName());
//
//        verify(categoryRepository).findById(any(Long.class));
//    }
}
