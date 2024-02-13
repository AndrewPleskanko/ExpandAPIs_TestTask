package com.example.backend.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.backend.entity.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDto {
    @NotEmpty(message = "Table name cannot be empty")
    private String table;

    @NotEmpty(message = "Product records cannot be empty")
    @Size(min = 3)
    private List<Product> records;

    public List<Product> getRecords() {
        return new ArrayList<>(records);
    }
}
