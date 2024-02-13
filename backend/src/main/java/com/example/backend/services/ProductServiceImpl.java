package com.example.backend.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.backend.dto.ProductDto;
import com.example.backend.entity.Product;
import com.example.backend.repositories.ProductRepository;
import com.example.backend.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for working with products.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * Save products.
     *
     * @param request DTO for saving products.
     */
    @Override
    public void saveProducts(ProductDto request) {
        log.info("Saving products: {}", request.getRecords());
        List<Product> records = request.getRecords();
        productRepository.saveAll(records);
        log.info("Products saved successfully");
    }

    /**
     * Retrieve all products.
     *
     * @return List of products.
     */
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        log.info("Retrieving all products");
        Page<Product> products = productRepository.findAll(pageable);
        log.info("Products retrieved successfully");
        return products;
    }
}