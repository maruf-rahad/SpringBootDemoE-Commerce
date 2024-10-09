package com.example.demomaven.services;

import com.example.demomaven.models.Product;
import com.example.demomaven.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    public Product getProductById(int id) {

        return productRepository.findById(id).orElse(new Product());
    }

    public void addProduct(Product product) {

        productRepository.save(product);
    }

    public void updateProduct(Product product) {

        productRepository.save(product);
    }

    public void deleteProduct(int id) {

        productRepository.deleteById(id);
    }

}