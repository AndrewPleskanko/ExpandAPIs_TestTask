package com.expandapis.productcatalog.controllers;

import com.expandapis.productcatalog.dto.ProductDTO;
import com.expandapis.productcatalog.entity.Product;
import com.expandapis.productcatalog.services.ProductServiceImp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A service for working with products
 */
@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Api(value = "Product Service", description = "A service for working with products", tags = {"Product"})
public class ProductController {
    private final ProductServiceImp productService;

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
        log.info("Entering {} method", "addProducts");
        productService.saveProducts(request);
        log.info("Records saved successfully");
        return ResponseEntity.ok("Records saved successfully");
    }

    /**
     * Gets all products from the database
     *
     * @return a response entity with a list of products or an error message
     */
    @GetMapping("all")
    @ApiOperation(value = "Get all products", notes = "Gets all products from the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved all products"),
            @ApiResponse(code = 400, message = "Invalid page number or size"),
            @ApiResponse(code = 500, message = "Error retrieving products")
    })
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Entering {} method", "getAllProducts");
        Page<Product> products = productService.getAllProducts(pageable);
        log.info("Retrieved all products");
        return ResponseEntity.ok(products);
    }
}
