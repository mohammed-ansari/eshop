package com.eshop.api.controller;

import com.eshop.api.entity.Customer;
import com.eshop.api.entity.Product;
import com.eshop.api.entity.Purchase;
import com.eshop.api.repo.CustomerRepository;
import com.eshop.api.repo.ProductRepository;
import com.eshop.api.repo.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    Purchase createPurchase(@RequestBody Purchase purchase) {
        if (purchase.getCustomer().getId() != null) {
            Optional<Customer> customer = customerRepository.findById(purchase.getCustomer().getId());
            purchase.setCustomer(customer.get());

            List<Product> updatedProducts = purchase.getProducts().stream().map(product -> {
                if (product.getId() != null) {
                    Optional<Product> foundProduct = productRepository.findById(product.getId());
                    product = foundProduct.get();
                }
                return product;
            }).collect(Collectors.toList());
            purchase.setProducts(updatedProducts);
            return purchaseRepository.save(purchase);
        }
        return null;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @GetMapping(path = "/search")
    public @ResponseBody
    Iterable<Purchase> findByCustomerId(@RequestParam Integer customerId) {
        return purchaseRepository.findByCustomerId(customerId);
    }
}
