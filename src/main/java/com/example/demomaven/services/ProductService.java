package com.example.demomaven.services;

import com.example.demomaven.models.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    List<Product> products = new ArrayList<>(Arrays.asList(
            new Product(1, "Iphone", 50000),
            new Product(2, "Samsung", 40000)
    ));

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProductById(int id) {
            return products.stream()
                    .filter(product -> product.getProductId() == id)
                    .findFirst().get();
     }

     public void addProduct(Product product) {
         System.out.println(product);
        products.add(product);
     }

     public void updateProduct(Product product) {
        for(int i = 0; i < products.size(); i++) {
            if(products.get(i).getProductId() == product.getProductId()) {
                products.set(i, product);
            }
        }
     }

     public void deleteProduct(int id) {
        products.stream().
                filter(product -> product.getProductId() == id)
                .findFirst().ifPresent(product -> products.remove(product));
     }
}
