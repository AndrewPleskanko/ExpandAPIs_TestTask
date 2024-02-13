package com.example.backend.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.backend.dto.ProductDto;
import com.example.backend.entity.Product;

public interface ProductService {
    void saveProducts(ProductDto request);

    Page<Product> getAllProducts(Pageable pageable);
}
