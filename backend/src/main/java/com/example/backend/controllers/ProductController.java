package com.example.backend.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.ProductDto;
import com.example.backend.entity.Product;
import com.example.backend.services.ProductServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A service for working with products.
 */

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Api(value = "Product Service", description = "A service for working with products", tags = {"Product"})
public class ProductController {

    private final ProductServiceImpl productService;

    /**
     * Adds products to the database.
     *
     * @param request the product DTO containing the list of products to add
     * @return a response entity with a success or error message
     */
    @PostMapping("add")
    @ApiOperation(value = "Add product", notes = "Adds products to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Records saved successfully"),
            @ApiResponse(code = 500, message = "Error saving records")
    })
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductDto request) {
        log.info("Request to add product" + request);
        productService.saveProducts(request);
        return ResponseEntity.ok("Records saved successfully");
    }

    /**
     * Gets all products from the database.
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
    @ResponseBody
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productService.getAllProducts(pageable);
        log.info("Retrieved all products");
        return ResponseEntity.ok(products);
    }
}
