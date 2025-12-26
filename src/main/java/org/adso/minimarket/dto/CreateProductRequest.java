package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be at most 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Price format is invalid")
    private String price;

    @JsonProperty("category_id")
    @NotNull(message = "Category is required")
    @Min(value = 1, message = "Invalid category")
    private Long categoryId;

    @Min(value = 1, message = "Stock has to be greater than 0")
    private Integer stock;
}
