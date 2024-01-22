package com.expandapis.productcatalog.controller;

import com.expandapis.productcatalog.controllers.ProductController;
import com.expandapis.productcatalog.dto.AuthResponseDto;
import com.expandapis.productcatalog.dto.ProductDto;
import com.expandapis.productcatalog.dto.UserDto;
import com.expandapis.productcatalog.entity.Product;
import com.expandapis.productcatalog.entity.Role;
import com.expandapis.productcatalog.security.*;
import com.expandapis.productcatalog.services.AuthenticationServiceImpl;
import com.expandapis.productcatalog.services.ProductServiceImpl;
import com.expandapis.productcatalog.utils.UserTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.expandapis.productcatalog.utils.TokenGenerationUtils.asJsonString;
import static com.expandapis.productcatalog.utils.TokenGenerationUtils.extractTokenFromResponse;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({WebSecurityConfig.class, JWTGenerator.class, JWTAuthEntryPoint.class, AuthenticationServiceImpl.class})
public class ProductControllerIntegrationTest {

    private List<Product> productList;
    private UserDto userDtoUser;
    private ProductDto productDTO;

    @MockBean
    private ProductServiceImpl productService;

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws ParseException {
        // Given
        userDtoUser = UserTestUtils.createUserDto("john", "123", Role.ROLE_USER);

        productList = Arrays.asList(
                new Product(1L, parseDateString(), "11111", "Fina Lika", 30, "Paid"),
                new Product(2L, parseDateString(), "11111", "Test Inventory 2", 20, "Paid")
        );

        productDTO = new ProductDto();
        productDTO.setTable("products");
        productDTO.setRecords(productList);

        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User("john", "$2a$10$5vvbROzmmXGkfPVaZTyNjODxOEBkobazyMcGWaoSKugSaMLSER0Pq", Collections.singleton(Role.ROLE_USER)));
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "john", "123", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        when(authenticationProvider.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        when(authenticationProvider.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        userDtoUser.getUsername(),
                        userDtoUser.getPassword()));
    }

    public static Date parseDateString() throws ParseException {
        String dateString = "03-01-2023";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.parse(dateString);
    }

    @Test
    @DisplayName("Test adding products to database")
    void testAddProducts_addsProductsToCart() throws Exception {
        // When
        doNothing().when(productService).saveProducts(any());
        String token = generateAuthToken();

        // Then
        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(asJsonString(productDTO)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Records saved successfully"));
    }

    @Test
    @DisplayName("Test getting all products")
    void testGetAllProducts_returnsAllProducts() throws Exception {
        // Given
        when(productService.getAllProducts(any(Pageable.class))).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(0);
            return new PageImpl<>(productList, pageable, productList.size());
        });

        String token = generateAuthToken();

        // Then
        mockMvc.perform(get("/products/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].itemName", is("Fina Lika")))
                .andExpect(jsonPath("$.content[1].itemName", is("Test Inventory 2")))
                .andExpect(jsonPath("$.content[0].status").value(productList.get(0).getStatus()))
                .andExpect(jsonPath("$.content[1].status").value(productList.get(1).getStatus()))
                .andExpect(jsonPath("$.content[0].itemQuantity").value(productList.get(0).getItemQuantity()))
                .andExpect(jsonPath("$.content[1].itemQuantity").value(productList.get(1).getItemQuantity()))
                .andExpect(jsonPath("$.content[0].itemCode").value(productList.get(0).getItemCode()))
                .andExpect(jsonPath("$.content[1].itemCode").value(productList.get(1).getItemCode()));
    }

    @Test
    @DisplayName("Should handle 500 Internal Server Error during product addition")
    public void testAddProduct_handlesInternalServerError() throws Exception {
        // When
        doThrow(new DataIntegrityViolationException("Error retrieving products"))
                .when(productService).getAllProducts(any(Pageable.class));

        String token = generateAuthToken();

        // Then
        mockMvc.perform(get("/products/all")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error retrieving products"));
    }

    private String generateAuthToken() throws Exception {
        AuthResponseDto response = authenticationService.authenticateUser(userDtoUser);
        ObjectMapper mapper = new ObjectMapper();
        return extractTokenFromResponse(mapper.writeValueAsString(response));
    }
}
