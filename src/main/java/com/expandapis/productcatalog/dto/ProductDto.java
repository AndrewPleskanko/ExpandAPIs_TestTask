package com.expandapis.productcatalog.dto;

import com.expandapis.productcatalog.entity.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class ProductDto {
    @NotEmpty(message = "Table name cannot be empty")
    private String table;

    @NotEmpty(message = "Product records cannot be empty")
    @Size(min = 3)
    private List<Product> records;
}
