package com.eshop.api.controller;

import com.eshop.api.entity.Customer;
import com.eshop.api.repo.CustomerRepository;
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

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CustomerRepository customerRepository;

    Customer customer1 = new Customer(1, "John", "Joensuu 80220");
    Customer customer2 = new Customer(2, "Alex", "Helsinki 80110");
    Customer customer3 = new Customer(3, "Jay", "Tampere 80330");

    @Test
    public void getAllCustomers_success() throws Exception {
        List<Customer> records = new ArrayList<>(Arrays.asList(customer1, customer2, customer3));

        Mockito.when(customerRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/customer/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("Jay")));
    }

    @Test
    public void getCustomerByName_success() throws Exception {
        Mockito.when(customerRepository.findByName(customer1.getName())).thenReturn(Arrays.asList(customer1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/customer/search?name=John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John")));
    }

    @Test
    public void createCustomer_success() throws Exception {
        Customer record = Customer.builder()
                .name("John")
                .address("Joensuu 80220")
                .build();

        Mockito.when(customerRepository.save(record)).thenReturn(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/customer/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(record));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("John")));
    }
}
