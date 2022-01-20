package com.eshop.api.controller;

import com.eshop.api.entity.Customer;
import com.eshop.api.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    Customer addCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping(path = "/search")
    public @ResponseBody
    Iterable<Customer> findByCustomerName(@RequestParam String name) {
        return customerRepository.findByName(name);
    }
}
