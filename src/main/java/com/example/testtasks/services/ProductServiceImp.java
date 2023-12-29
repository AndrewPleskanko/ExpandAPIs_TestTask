package com.example.testtasks.services;

import com.example.testtasks.dto.ProductDTO;
import com.example.testtasks.entity.Product;
import com.example.testtasks.repositories.ProductRepository;
import com.example.testtasks.services.interfaces.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for working with products.
 */
@Slf4j
@Service
public class ProductServiceImp implements ProductService {

    private static final String SAVING_PRODUCTS_MESSAGE = "Saving products: {}";
    private static final String PRODUCTS_SAVED_SUCCESSFULLY_MESSAGE = "Products saved successfully";
    private static final String ERROR_SAVING_PRODUCTS_MESSAGE = "Error saving products";
    private static final String RETRIEVING_ALL_PRODUCTS_MESSAGE = "Retrieving all products";
    private static final String PRODUCTS_RETRIEVED_SUCCESSFULLY_MESSAGE = "Products retrieved successfully";
    private static final String ERROR_RETRIEVING_PRODUCTS_MESSAGE = "Error retrieving products";

    @Autowired
    private ProductRepository productRepository;

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
        try {
            log.info(SAVING_PRODUCTS_MESSAGE, request.getRecords());
            List<Product> records = request.getRecords();
            productRepository.saveAll(records);
            log.info(PRODUCTS_SAVED_SUCCESSFULLY_MESSAGE);
        } catch (Exception e) {
            log.error(ERROR_SAVING_PRODUCTS_MESSAGE, e);
            throw e;
        }
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
    public List<Product> getAllProducts() {
        try {
            log.info(RETRIEVING_ALL_PRODUCTS_MESSAGE);
            List<Product> products = productRepository.findAll();
            log.info(PRODUCTS_RETRIEVED_SUCCESSFULLY_MESSAGE);
            return products;
        } catch (Exception e) {
            log.error(ERROR_RETRIEVING_PRODUCTS_MESSAGE, e);
            throw e;
        }
    }
}