package com.expandapis.productcatalog;

import com.expandapis.productcatalog.controllers.ProductController;
import com.expandapis.productcatalog.dto.ProductDTO;
import com.expandapis.productcatalog.entity.Product;
import com.expandapis.productcatalog.services.ProductServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductControllerTest {

    @Mock
    private ProductServiceImp productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void testAddProducts() {
        // Arrange
        ProductDTO request = new ProductDTO();
        doNothing().when(productService).saveProducts(request);

        // Act
        ResponseEntity<String> responseEntity = productController.addProducts(request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Records saved successfully", responseEntity.getBody());
        verify(productService, times(1)).saveProducts(request);
    }

    @Test
    void testAddProductsError() {
        // Arrange
        ProductDTO request = new ProductDTO();
        doThrow(new RuntimeException("Test exception")).when(productService).saveProducts(request);

        // Act
        ResponseEntity<String> responseEntity = productController.addProducts(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error saving records", responseEntity.getBody());
        verify(productService, times(1)).saveProducts(request);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Page<Product> products = new PageImpl<>(Collections.singletonList(new Product()));

        when(productService.getAllProducts(any(Pageable.class))).thenReturn(products);

        // Act
        ResponseEntity<Page<Product>> responseEntity = productController.getAllProducts(0, 10);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(products, responseEntity.getBody());
        verify(productService, times(1)).getAllProducts(any(Pageable.class));
    }

    @Test
    void testGetAllProductsError() {
        // Arrange
        when(productService.getAllProducts(any(Pageable.class))).thenThrow(new RuntimeException("Test exception"));

        // Act
        ResponseEntity<Page<Product>> responseEntity = productController.getAllProducts(0, 10);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        verify(productService, times(1)).getAllProducts(any(Pageable.class));
    }
}
