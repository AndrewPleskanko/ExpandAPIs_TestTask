package com.expandapis.productcatalog.service;

import com.expandapis.productcatalog.dto.ProductDTO;
import com.expandapis.productcatalog.entity.Product;
import com.expandapis.productcatalog.repositories.ProductRepository;
import com.expandapis.productcatalog.services.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    public static Date parseDateString() throws ParseException {
        String dateString = "03-01-2023";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.parse(dateString);
    }

    @Test
    public void testSaveProducts() throws ParseException {
        // Arrange
        ProductDTO request = new ProductDTO();
        List<Product> records = Arrays.asList(
                new Product(1L, parseDateString(), "11111", "Fina Lika", 30, "Paid"),
                new Product(2L, parseDateString(), "11111", "Test Inventory 2", 20, "Paid")
        );
        request.setRecords(records);

        when(productRepository.saveAll(records)).thenReturn(records);

        // Act
        productService.saveProducts(request);

        // Assert
        verify(productRepository).saveAll(records);

    }

    @Test
    public void testGetAllProducts() throws ParseException {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(1L, parseDateString(), "11111", "Fina Lika", 30, "Paid"),
                new Product(2L, parseDateString(), "11111", "Test Inventory 2", 20, "Paid")
        );

        when(productRepository.findAll()).thenReturn(products);

        // Act
        Page<Product> result = productService.getAllProducts(any(Pageable.class));

        // Assert
        assertEquals(products, result);
    }
}