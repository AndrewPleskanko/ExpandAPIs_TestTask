package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.ProductDto;
import com.example.backend.entity.Product;
import com.example.backend.repositories.ProductRepository;
import com.example.backend.services.ProductServiceImpl;

@SpringBootTest
public class ProductServiceIntegrationTest extends BaseServiceTest {

    private ProductDto productDto;
    private List<Product> products;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() throws ParseException {
        // Given
        productDto = new ProductDto();
        products = Arrays.asList(
                new Product(1L, parseDateString(), "11111", "Fina Lika", 30, "Paid"),
                new Product(2L, parseDateString(), "11111", "Test Inventory 2", 20, "Paid")
        );
        productDto.setRecords(products);
    }

    @Test
    @DisplayName("Save products to the database")
    @Transactional
    public void saveProducts_WithProductDto_SavesProductsToRepository() {
        // When
        productService.saveProducts(productDto);
        List<Product> actualProducts = productRepository.findAll();

        // Then
        assertEquals(products, actualProducts, "The lists of products should match");
    }

    @Test
    @DisplayName("Get all products from the database")
    @Transactional
    public void getProducts_withSavedProducts_returnsProducts() {
        // When
        productService.saveProducts(productDto);
        List<Product> actualProducts = productService.getAllProducts(Pageable.unpaged()).getContent();

        // Then
        assertAll(
                () -> assertEquals(products.size(), actualProducts.size(),
                        "The page should have the same number of elements as the expected list"),
                () -> assertTrue(actualProducts.containsAll(products),
                        "The page should contain all the expected products"));
    }

    public static Date parseDateString() throws ParseException {
        String dateString = "03-01-2023";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.parse(dateString);
    }
}