package com.expandapis.productcatalog.service;

import com.expandapis.productcatalog.dto.ProductDTO;
import com.expandapis.productcatalog.entity.Product;
import com.expandapis.productcatalog.repositories.ProductRepository;
import com.expandapis.productcatalog.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class ProductServiceIntegrationTest {

    private ProductDTO productDTO;
    private List<Product> products;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductRepository productRepository;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    @BeforeEach
    public void setup() throws ParseException {
        // Given
        productDTO = new ProductDTO();
        products = Arrays.asList(
                new Product(1L, parseDateString(), "11111", "Fina Lika", 30, "Paid"),
                new Product(2L, parseDateString(), "11111", "Test Inventory 2", 20, "Paid")
        );
        productDTO.setRecords(products);
    }

    @Test
    @DisplayName("Save products to the database")
    public void saveProducts_WithProductDTO_SavesProductsToRepository() {
        // When
        productService.saveProducts(productDTO);
        List<Product> actualProducts = productRepository.findAll();
        // Then
        assertEquals(products, actualProducts, "The lists of products should match");
    }

    @Test
    @DisplayName("Get all products from the database")
    public void getProducts_withSavedProducts_returnsProducts() {
        // When
        productService.saveProducts(productDTO);
        List<Product> actualProducts = productService.getAllProducts(Pageable.unpaged()).getContent();
        // Then
        assertAll(
                () -> assertEquals(products.size(), actualProducts.size(), "The page should have the same number of elements as the expected list"),
                () -> assertTrue(actualProducts.containsAll(products), "The page should contain all the expected products")
        );
    }

    public static Date parseDateString() throws ParseException {
        String dateString = "03-01-2023";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.parse(dateString);
    }
}