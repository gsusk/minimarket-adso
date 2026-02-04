package org.adso.minimarket.models;

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

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public Category(Long id, String name, List<Map<String, Object>> attributeDefinitions) {
        this.id = id;
        this.name = name;
        this.attributeDefinitions = attributeDefinitions;
    }

    public Category(String name, List<Map<String, Object>> attributeDefinitions) {
        this.name = name;
        this.attributeDefinitions = attributeDefinitions;
    }
}
