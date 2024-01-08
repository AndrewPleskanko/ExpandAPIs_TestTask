package com.expandapis.productcatalog.controller;

import com.expandapis.productcatalog.controllers.ProductController;
import com.expandapis.productcatalog.dto.ProductDTO;
import com.expandapis.productcatalog.entity.Product;
import com.expandapis.productcatalog.services.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
public class ProductControllerIntegrationTest {

    @Mock
    private ProductServiceImpl productService;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"));
    @InjectMocks
    private ProductController productController;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    public static Date parseDateString() throws ParseException {
        String dateString = "03-01-2023";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.parse(dateString);
    }

    @Test
    void testAddProducts() throws Exception {
        // Arrange
        List<Product> productList = createTestProducts();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTable("products");
        productDTO.setRecords(productList);
        // Act
        doNothing().when(productService).saveProducts(any());
        // Assert
        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void testGetAllProducts() throws Exception {
        // Arrange
        List<Product> productList = createTestProducts();
        // Act
        when(productService.getAllProducts(any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(0);
            return new PageImpl<>(productList, pageable, productList.size());
        });
        // Assert
        mockMvc.perform(get("/products/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].itemName", is("Fina Lika")))
                .andExpect(jsonPath("$.content[1].itemName", is("Test Inventory 2")));
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

    private List<Product> createTestProducts() throws ParseException {
        // Arrange
        Product product1 = new Product();
        product1.setEntryDate(parseDateString());
        product1.setItemCode("11111");
        product1.setItemName("Fina Lika");
        product1.setItemQuantity(30);
        product1.setStatus("Paid");

        Product product2 = new Product();
        product2.setEntryDate(parseDateString());
        product2.setItemCode("11111");
        product2.setItemName("Test Inventory 2");
        product2.setItemQuantity(20);
        product2.setStatus("Paid");

        return Arrays.asList(product1, product2);
    }
}
