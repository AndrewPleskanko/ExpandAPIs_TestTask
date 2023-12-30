package com.expandapis.productcatalog.services;

import com.expandapis.productcatalog.dto.ProductDTO;
import com.expandapis.productcatalog.entity.Product;
import com.expandapis.productcatalog.repositories.ProductRepository;
import com.expandapis.productcatalog.services.interfaces.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    /**
     * Save products.
     *
     * @param request DTO for saving products.
     */
    @ApiOperation(value = "Save products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products saved successfully"),
            @ApiResponse(code = 500, message = "Error saving products")
    })
    @Override
    public void saveProducts(ProductDTO request) {
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
    @ApiOperation(value = "Retrieve all products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products retrieved successfully"),
            @ApiResponse(code = 500, message = "Error retrieving products")
    })
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        log.info("Retrieving all products");
        Page<Product> products = productRepository.findAll(pageable);
        log.info("Products retrieved successfully");
        return products;
    }
}