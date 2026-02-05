package org.adso.minimarket.validation;

import lombok.extern.slf4j.Slf4j;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.repository.jpa.CategoryRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AttributeSchemaValidator {

    private final CategoryRepository categoryRepository;
    
    private final Map<String, Set<String>> categoryAttributesCache = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> facetableAttributesCache = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> filterableAttributesCache = new ConcurrentHashMap<>();
    
    private final Map<String, Map<String, AttributeDefinition>> attributeDefinitionsCache = new ConcurrentHashMap<>();
    
    private final Set<String> globalAttributesCache = new HashSet<>();
    private final Set<String> globalFacetableAttributesCache = new HashSet<>();
    private final Set<String> globalFilterableAttributesCache = new HashSet<>();

    public AttributeSchemaValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadSchema() {
        log.info("loading attribute schema into memry..");
        List<Category> allCategories = categoryRepository.findAll();

        for (Category category : allCategories) {
            String categoryKey = category.getName().toLowerCase();
            
            Set<String> attributes = new HashSet<>();
            Set<String> facetableAttributes = new HashSet<>();
            Set<String> filterableAttributes = new HashSet<>();
            Map<String, AttributeDefinition> definitions = new HashMap<>();
            
            List<Map<String, Object>> definitionMaps = category.getAllAttributeDefinitions();

            if (definitionMaps != null) {
                for (Map<String, Object> defMap : definitionMaps) {
                    try {
                        AttributeDefinition def = AttributeDefinition.fromMap(defMap);
                        String attrName = def.getName();
                        
                        attributes.add(attrName);
                        definitions.put(attrName, def);
                        
                        if (def.isFacetable()) {
                            facetableAttributes.add(attrName);
                        }
                        
                        if (def.isFilterable()) {
                            filterableAttributes.add(attrName);
                        }
                    } catch (Exception e) {
                        log.warn("Failed to parse attribute definition for category '{}': {}", 
                                category.getName(), e.getMessage());
                    }
                }
            }

            categoryAttributesCache.put(categoryKey, attributes);
            facetableAttributesCache.put(categoryKey, facetableAttributes);
            filterableAttributesCache.put(categoryKey, filterableAttributes);
            attributeDefinitionsCache.put(categoryKey, definitions);
            globalAttributesCache.addAll(attributes);
            globalFacetableAttributesCache.addAll(facetableAttributes);
            globalFilterableAttributesCache.addAll(filterableAttributes);
        }
        
        log.info("Loaded schema for {} categories with {} total unique attributes ({} filterable, {} facetable).",
                categoryAttributesCache.size(), 
                globalAttributesCache.size(),
                globalFilterableAttributesCache.size(),
                globalFacetableAttributesCache.size());
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

    public Set<String> getFilterableAttributes(String categoryName) {
        if (categoryName == null) {
            return globalFilterableAttributesCache;
        }
        return filterableAttributesCache.getOrDefault(categoryName.toLowerCase(), globalFilterableAttributesCache);
    }

    public Optional<AttributeDefinition> getAttributeDefinition(String categoryName, String attributeName) {
        if (categoryName == null || attributeName == null) {
            return Optional.empty();
        }
        
        Map<String, AttributeDefinition> categoryDefs = attributeDefinitionsCache.get(categoryName.toLowerCase());
        if (categoryDefs == null) {
            return Optional.empty();
        }
        
        return Optional.ofNullable(categoryDefs.get(attributeName));
    }

    public Map<String, AttributeDefinition> getAttributeDefinitions(String categoryName) {
        if (categoryName == null) {
            return Collections.emptyMap();
        }
        return attributeDefinitionsCache.getOrDefault(categoryName.toLowerCase(), Collections.emptyMap());
    }

    public FacetStrategy getFacetStrategy(String categoryName, String attributeName) {
        return getAttributeDefinition(categoryName, attributeName)
                .map(AttributeDefinition::getFacetStrategy)
                .orElse(FacetStrategy.NONE);
    }

    public FilterType getFilterType(String categoryName, String attributeName) {
        return getAttributeDefinition(categoryName, attributeName)
                .map(AttributeDefinition::getFilterType)
                .orElse(FilterType.NONE);
    }
}
