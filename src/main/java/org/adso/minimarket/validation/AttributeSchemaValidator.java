package org.adso.minimarket.validation;

import lombok.extern.slf4j.Slf4j;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.repository.jpa.CategoryRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AttributeSchemaValidator {

    private final CategoryRepository categoryRepository;
    private final Map<String, Set<String>> categoryAttributesCache = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> facetableAttributesCache = new ConcurrentHashMap<>();
    private final Set<String> globalAttributesCache = new HashSet<>();
    private final Set<String> globalFacetableAttributesCache = new HashSet<>();

    public AttributeSchemaValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadSchema() {
        log.info("Loading attribute schema into memory...");
        List<Category> allCategories = categoryRepository.findAll();

        for (Category category : allCategories) {
            Set<String> attributes = new HashSet<>();
            Set<String> facetableAttributes = new HashSet<>();
            List<Map<String, Object>> definitions = category.getAllAttributeDefinitions();

            if (definitions != null) {
                for (Map<String, Object> def : definitions) {
                    AttributeDefinition attributeDefinition = AttributeDefinition.fromMap(def);
                    attributes.add(attributeDefinition.getName());
                    if (attributeDefinition.isFacetable()) {
                        facetableAttributes.add(attributeDefinition.getName());
                    }
                }
            }

            categoryAttributesCache.put(category.getName().toLowerCase(), attributes);
            facetableAttributesCache.put(category.getName().toLowerCase(), facetableAttributes);
            globalAttributesCache.addAll(attributes);
            globalFacetableAttributesCache.addAll(facetableAttributes);
        }
        log.info("Loaded schema for {} categories with {} total unique attributes.",
                categoryAttributesCache.size(), globalAttributesCache.size());
    }

    public Set<String> getAllowedAttributes(String categoryName) {
        if (categoryName == null) {
            return globalAttributesCache;
        }
        return categoryAttributesCache.getOrDefault(categoryName.toLowerCase(), globalAttributesCache);
    }

    public Set<String> getFacetableAttributes(String categoryName) {
        if (categoryName == null) {
            return globalFacetableAttributesCache;
        }
        return facetableAttributesCache.getOrDefault(categoryName.toLowerCase(), globalFacetableAttributesCache);
    }
}
