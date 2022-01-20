package com.eshop.api.controller;

import com.eshop.api.entity.Customer;
import com.eshop.api.entity.Product;
import com.eshop.api.entity.Purchase;
import com.eshop.api.repo.CustomerRepository;
import com.eshop.api.repo.ProductRepository;
import com.eshop.api.repo.PurchaseRepository;
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

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseController.class)
public class PurchaseControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    PurchaseRepository purchaseRepository;
    @MockBean
    CustomerRepository customerRepository;
    @MockBean
    ProductRepository productRepository;

    Customer customer = new Customer(1, "John", "Joensuu 80220");
    Purchase purchase1 = new Purchase(1, new Date(), null, customer);
    Purchase purchase2 = new Purchase(2, new Date(), null, customer);
    Purchase purchase3 = new Purchase(3, new Date(), null, customer);

    @Test
    public void getAllPurchases_success() throws Exception {
        List<Purchase> records = new ArrayList<>(Arrays.asList(purchase1, purchase2, purchase3));

        Mockito.when(purchaseRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/purchase/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void getPurchaseByCustomerId_success() throws Exception {
        Mockito.when(purchaseRepository.findByCustomerId(customer.getId())).thenReturn(Arrays.asList(purchase1, purchase2, purchase3));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/purchase/search?customerId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void createPurchase_success() throws Exception {
        Product product1 = new Product(1, "XYZ", "129 eur", null);

        Purchase record = Purchase.builder()
                .products(Arrays.asList(product1))
                .customer(customer)
                .build();
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.ofNullable(customer));
        Mockito.when(productRepository.findById(product1.getId())).thenReturn(Optional.of(product1));


        Mockito.when(purchaseRepository.save(record)).thenReturn(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/purchase/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(record));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.customer.id", is(1)))
                .andExpect(jsonPath("$.products.[0].id", is(1)));

    }
}

