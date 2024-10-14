package com.example.demomaven.services;

import com.example.demomaven.models.Product;
import com.example.demomaven.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public List<Product> searchProducts(String kewword) {
        return productRepository.searchProducts(kewword);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {

        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return productRepository.save(product);
    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {

        Product checkIfAvailable = productRepository.findById(id).orElse(null);
        if(checkIfAvailable == null) {
            return null;
        }
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        productRepository.save(product);
        return product;
    }

    public void deleteProduct(int id) {

        productRepository.deleteById(id);
    }

}