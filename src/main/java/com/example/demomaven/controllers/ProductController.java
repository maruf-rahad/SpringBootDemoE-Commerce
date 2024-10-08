package com.example.demomaven.controllers;

import com.example.demomaven.models.Product;
import com.example.demomaven.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    private List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    private Product getProductById(@PathVariable int id){
        return productService.getProductById(id);
    }

    @PostMapping("products")
    private void addProduct(@RequestBody Product product){
        productService.addProduct(product);
    }

    @PutMapping("products")
    public void updateProduct(@RequestBody Product product){
        productService.updateProduct(product);
    }

    @DeleteMapping("products/{id}")
    public void deleteProduct(@PathVariable int id){
        productService.deleteProduct(id);
    }


}
