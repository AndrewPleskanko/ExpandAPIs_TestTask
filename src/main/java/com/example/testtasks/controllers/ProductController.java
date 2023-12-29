package com.example.testtasks.controllers;

import com.example.testtasks.dto.ProductDTO;
import com.example.testtasks.entity.Product;
import com.example.testtasks.handler.GlobalExceptionHandler;
import com.example.testtasks.services.ProductServiceImp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A service for working with products
 */
@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Api(value = "Product Service", description = "A service for working with products", tags = {"Product"})
public class ProductController {
    public static final String RECORDS_SAVED_SUCCESSFULLY = "Records saved successfully";
    public static final String ENTERING_METHOD_MESSAGE = "Entering {} method";
    public static final String ERROR_SAVING_RECORDS_MESSAGE = "Error saving records";
    public static final String RETRIEVED_ALL_PRODUCTS_MESSAGE = "Retrieved all products";

    private final ProductServiceImp productService;
    private final GlobalExceptionHandler globalExceptionHandler;

    /**
     * Adds products to the database
     *
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
        log.info(ENTERING_METHOD_MESSAGE, "addProducts");
        try {
            productService.saveProducts(request);
            log.info(RECORDS_SAVED_SUCCESSFULLY);
            return ResponseEntity.ok(RECORDS_SAVED_SUCCESSFULLY);
        } catch (Exception e) {
            return globalExceptionHandler.handleStringException(e);
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
        log.info(ENTERING_METHOD_MESSAGE, "getAllProducts");
        try {
            List<Product> products = productService.getAllProducts();
            log.info(RETRIEVED_ALL_PRODUCTS_MESSAGE);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return new GlobalExceptionHandler().handleListProductException(e);
        }
    }
}
