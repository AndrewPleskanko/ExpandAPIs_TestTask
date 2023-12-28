package com.example.testtasks.controllers;

import com.example.testtasks.dto.ProductDTO;
import com.example.testtasks.entity.Product;
import com.example.testtasks.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A service for working with products
 */
@RestController
@RequestMapping("/products")
@Api(value = "Product Service", description = "A service for working with products", tags = {"Product"})
public class ProductController {
    private static final Logger logger = LogManager.getLogger();
    public static final String RECORDS_SAVED_SUCCESSFULLY = "Records saved successfully";
    public static final String ENTERING_METHOD_MESSAGE = "Entering {} method";
    public static final String EXITING_METHOD_MESSAGE = "Exiting {} method";
    public static final String ERROR_SAVING_RECORDS_MESSAGE = "Error saving records";
    public static final String ERROR_RETRIEVING_PRODUCTS_MESSAGE = "Error retrieving products";
    public static final String RETRIEVED_ALL_PRODUCTS_MESSAGE = "Retrieved all products";

    private final ProductService productService;

    /**
     * Constructs a new product controller with the given product service
     * @param productService the product service to use
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Adds products to the database
     * @param request the product DTO containing the list of products to add
     * @return a response entity with a success or error message
     */
    @PostMapping("add")
    @ApiOperation(value = "Add products", notes = "Adds products to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Records saved successfully"),
            @ApiResponse(code = 500, message = "Error saving records")
    })
    public ResponseEntity<String> addProducts(@RequestBody ProductDTO request) {
        logger.info(ENTERING_METHOD_MESSAGE, "addProducts");
        try {
            productService.saveProducts(request);
            logger.info(RECORDS_SAVED_SUCCESSFULLY);
            return ResponseEntity.ok(RECORDS_SAVED_SUCCESSFULLY);
        } catch (Exception e) {
            logger.error(ERROR_SAVING_RECORDS_MESSAGE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_SAVING_RECORDS_MESSAGE);
        } finally {
            logger.info(EXITING_METHOD_MESSAGE, "addProducts");
        }
    }

    /**
     * Gets all products from the database
     *
     * @return a response entity with a list of products or an error message
     */
    @PostMapping("all")
    @ApiOperation(value = "Get all products", notes = "Gets all products from the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved all products"),
            @ApiResponse(code = 500, message = "Error retrieving products")
    })
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info(ENTERING_METHOD_MESSAGE, "getAllProducts");
        try {
            List<Product> products = productService.getAllProducts();
            logger.info(RETRIEVED_ALL_PRODUCTS_MESSAGE);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error(ERROR_RETRIEVING_PRODUCTS_MESSAGE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {
            logger.info(EXITING_METHOD_MESSAGE, "getAllProducts");
        }
    }
}
