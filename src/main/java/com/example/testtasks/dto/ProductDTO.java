package com.example.testtasks.dto;

import com.example.testtasks.entity.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    @NotEmpty(message = "Table name cannot be empty")
    private String table;

    @Valid
    @NotEmpty(message = "Product records cannot be empty")
    private List<Product> records;
}
