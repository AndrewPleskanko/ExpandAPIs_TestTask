package com.example.testtasks;

import com.example.testtasks.controllers.ProductController;
import com.example.testtasks.dto.ProductDTO;
import com.example.testtasks.entity.Product;
import com.example.testtasks.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductControllerTest {

    @Mock
    private ProductService productService;

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
        assertEquals(ProductController.RECORDS_SAVED_SUCCESSFULLY, responseEntity.getBody());
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
        assertEquals(ProductController.ERROR_SAVING_RECORDS_MESSAGE, responseEntity.getBody());
        verify(productService, times(1)).saveProducts(request);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        List<Product> products = Collections.singletonList(new Product());
        when(productService.getAllProducts()).thenReturn(products);

        // Act
        ResponseEntity<List<Product>> responseEntity = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(products, responseEntity.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetAllProductsError() {
        // Arrange
        when(productService.getAllProducts()).thenThrow(new RuntimeException("Test exception"));

        // Act
        ResponseEntity<List<Product>> responseEntity = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        verify(productService, times(1)).getAllProducts();
    }
}
