package com.eshop.api.controller;

import com.eshop.api.entity.Product;
import com.eshop.api.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    Product addProduct(@RequestBody Product product) {
        if (product.getName() != null && product.getPrice() != null) {
            product = productRepository.save(product);
            return product;
        }
        return null;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping(path = "/search")
    public @ResponseBody
    Iterable<Product> findByCustomerId(@RequestParam String name) {
        return productRepository.findByName(name);
    }
}
