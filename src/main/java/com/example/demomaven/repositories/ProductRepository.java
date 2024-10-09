package com.example.demomaven.repositories;

import com.example.demomaven.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product, Integer> {

}
