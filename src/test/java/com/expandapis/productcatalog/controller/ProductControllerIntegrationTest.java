package com.expandapis.productcatalog.controller;

import com.expandapis.productcatalog.controllers.ProductController;
import com.expandapis.productcatalog.dto.ProductDTO;
import com.expandapis.productcatalog.entity.Product;
import com.expandapis.productcatalog.services.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerIntegrationTest {

    private List<Product> productList;

    @MockBean
    private ProductServiceImpl productService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws ParseException {
        // Given
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

        productList = Arrays.asList(product1, product2);
    }

    public static Date parseDateString() throws ParseException {
        String dateString = "03-01-2023";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.parse(dateString);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("Test adding products to database")
    void testAddProducts_addsProductsToCart() throws Exception {
        // Given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTable("products");
        productDTO.setRecords(productList);

        // When
        doNothing().when(productService).saveProducts(any());

        // Then
        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDTO)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Records saved successfully"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Test getting all products")
    void testGetAllProducts_returnsAllProducts() throws Exception {
        // When
        when(productService.getAllProducts(any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(0);
            return new PageImpl<>(productList, pageable, productList.size());
        });

        // Then
        mockMvc.perform(get("/products/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].itemName", is("Fina Lika")))
                .andExpect(jsonPath("$.content[1].itemName", is("Test Inventory 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status").value(productList.get(0).getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].status").value(productList.get(1).getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemQuantity").value(productList.get(0).getItemQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].itemQuantity").value(productList.get(1).getItemQuantity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemCode").value(productList.get(0).getItemCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].itemCode").value(productList.get(1).getItemCode()));
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
