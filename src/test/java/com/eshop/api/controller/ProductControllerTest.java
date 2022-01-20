package com.eshop.api.controller;

import com.eshop.api.entity.Product;
import com.eshop.api.repo.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ProductRepository productRepository;

    Product product1 = new Product(1, "XYZ", "129 eur", null);
    Product product2 = new Product(2, "PQR", "230 eur", null);
    Product product3 = new Product(3, "LMN", "450 eur", null);

    @Test
    public void getAllProducts_success() throws Exception {
        List<Product> records = new ArrayList<>(Arrays.asList(product1, product2, product3));

        Mockito.when(productRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/product/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("LMN")));
    }

    @Test
    public void getProductByName_success() throws Exception {
        Mockito.when(productRepository.findByName(product1.getName())).thenReturn(Arrays.asList(product1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/product/search?name=XYZ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("XYZ")));
    }

    @Test
    public void createProduct_success() throws Exception {
        Product record = Product.builder()
                .name("XYZ")
                .price("129 eur")
                .build();

        Mockito.when(productRepository.save(record)).thenReturn(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(record));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("XYZ")));
    }


}
