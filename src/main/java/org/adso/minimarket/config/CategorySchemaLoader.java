package org.adso.minimarket.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.repository.jpa.CategoryRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CategorySchemaLoader {

    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    public CategorySchemaLoader(CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void loadCategorySchemas() {
        log.info("Loading category schemas from cat_attr.json...");

        try {
            List<CategorySchemaDto> schemas = readSchemasFromFile();
            Map<String, Category> categoryMap = buildCategoryMap();

            int updatedCount = 0;
            for (CategorySchemaDto schema : schemas) {
                updatedCount += processSchema(schema, categoryMap, null);
            }

            log.info("Successfully updated {} categories with new attribute schemas", updatedCount);

        } catch (Exception e) {
            log.error("Failed to load category schemas: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load category schemas", e);
        }
    }

    private List<CategorySchemaDto> readSchemasFromFile() throws IOException {
        ClassPathResource resource = new ClassPathResource("cat_attr.json");

        if (!resource.exists()) {
            throw new IOException("cat_attr.json not found in classpath");
        }

        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<List<CategorySchemaDto>>() {
            });
        }
    }

    private Map<String, Category> buildCategoryMap() {
        List<Category> allCategories = categoryRepository.findAll();
        Map<String, Category> map = new HashMap<>();

        for (Category category : allCategories) {
            map.put(category.getName(), category);
        }

        return map;
    }

    private int processSchema(CategorySchemaDto schema, Map<String, Category> categoryMap, String parentName) {
        int count = 0;

        Category category = categoryMap.get(schema.getName());

        if (category == null) {
            log.warn("Category '{}' not found in database, skipping schema update", schema.getName());
        } else {
            category.setAttributeDefinitions(schema.getAttributeDefinitions());
            categoryRepository.save(category);
            count++;

            log.debug("Updated schema for category: {} with {} attributes",
                    schema.getName(),
                    schema.getAttributeDefinitions() != null ? schema.getAttributeDefinitions().size() : 0);
        }

        if (schema.getChildren() != null && !schema.getChildren().isEmpty()) {
            for (CategorySchemaDto child : schema.getChildren()) {
                count += processSchema(child, categoryMap, schema.getName());
            }
        }

        return count;
    }

    private static class CategorySchemaDto {
        private String name;
        private List<Map<String, Object>> attributeDefinitions;
        private List<CategorySchemaDto> children;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Map<String, Object>> getAttributeDefinitions() {
            return attributeDefinitions;
        }

        public void setAttributeDefinitions(List<Map<String, Object>> attributeDefinitions) {
            this.attributeDefinitions = attributeDefinitions;
        }

        public List<CategorySchemaDto> getChildren() {
            return children;
        }

        public void setChildren(List<CategorySchemaDto> children) {
            this.children = children;
        }
    }
}
