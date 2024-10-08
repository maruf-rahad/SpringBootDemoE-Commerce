package com.example.demomaven.services;

import com.example.demomaven.models.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    List<Product> products = Arrays.asList(new Product(1, "Iphone", 50000),
            new Product(2, "Samsung", 40000));

    public List<Product> getAllProducts() {
        return products;
    }
}
