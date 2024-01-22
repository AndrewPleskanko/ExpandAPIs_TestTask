package com.expandapis.productcatalog.services;

import com.expandapis.productcatalog.dto.ProductDto;
import com.expandapis.productcatalog.entity.Product;
import com.expandapis.productcatalog.repositories.ProductRepository;
import com.expandapis.productcatalog.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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