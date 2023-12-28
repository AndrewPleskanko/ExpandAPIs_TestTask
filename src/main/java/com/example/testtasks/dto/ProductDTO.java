package com.example.testtasks.dto;

import com.example.testtasks.entity.Product;
import lombok.Data;
import java.util.List;

@Data
public class ProductDTO {
    private String table;
    private List<Product> records;
}
