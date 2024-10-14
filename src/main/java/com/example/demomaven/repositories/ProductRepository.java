package com.example.demomaven.repositories;

import com.example.demomaven.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Integer> {

    @Query("SELECT p from Product p WHERE " +
    "LOWER(p.productName) LIKE LOWER(concat('%', :kewword, '%')) OR " +
    "LOWER(p.productBrand) LIKE LOWER(concat('%', :kewword, '%')) OR " +
    "LOWER(p.productCategory) LIKE LOWER(concat('%', :kewword, '%')) OR " +
    "LOWER(p.productDescription) LIKE LOWER(concat('%', :kewword, '%'))"
    )
    List<Product> searchProducts(String keyword);
}
