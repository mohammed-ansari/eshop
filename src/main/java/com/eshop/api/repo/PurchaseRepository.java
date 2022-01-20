package com.eshop.api.repo;

import com.eshop.api.entity.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {
    List<Purchase> findByCustomerId(Integer customerId);
}
