package org.adso.minimarket.models.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Setting(settingPath = "elasticsearch-settings.json")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "products")
public class ProductDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String name;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Integer)
    private Integer stock;

    @Field(type = FieldType.Object)
    private Map<String, Object> specifications;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private LocalDateTime createdAt;
}