package org.adso.minimarket.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.adso.minimarket.models.product.Product;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attribute_definitions", columnDefinition = "json")
    private List<Map<String, Object>> attributeDefinitions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    
    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Category> subcategories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public Category(Long id, String name, List<Map<String, Object>> attributeDefinitions, Category parent) {
        this.id = id;
        this.name = name;
        this.attributeDefinitions = attributeDefinitions;
        this.parent = parent;
    }

    public Category(String name, List<Map<String, Object>> attributeDefinitions, Category parent) {
        this.name = name;
        this.attributeDefinitions = attributeDefinitions;
        this.parent = parent;
    }

    public String getFullPath() {
        if (parent == null) {
            return name;
        }
        return parent.getFullPath() + " > " + name;
    }

    public List<Map<String, Object>> getAllAttributeDefinitions() {
        List<Map<String, Object>> allDefinitions = new ArrayList<>();
        if (parent != null) {
            allDefinitions.addAll(parent.getAllAttributeDefinitions());
        }
        if (attributeDefinitions != null) {
            allDefinitions.addAll(attributeDefinitions);
        }
        return allDefinitions;
    }
}
