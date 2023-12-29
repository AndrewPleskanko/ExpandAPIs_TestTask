package com.expandapis.productcatalog.dto;

import com.expandapis.productcatalog.entity.Product;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    @NotEmpty(message = "Table name cannot be empty")
    private String table;

    @NotEmpty(message = "Product records cannot be empty")
    private List<Product> records;
}
