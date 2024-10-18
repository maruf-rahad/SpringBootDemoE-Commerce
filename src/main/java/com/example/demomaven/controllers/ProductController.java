package com.example.demomaven.controllers;

import com.example.demomaven.models.Product;
import com.example.demomaven.repositories.ProductRepository;
import com.example.demomaven.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    private String ProductControllerCheck(HttpServletRequest request) {
        return "Product Controller is working, session id: " + request.getSession().getId();
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }



    @GetMapping("/product")
    private ResponseEntity<List<Product>> getAllProducts() {

        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    private ResponseEntity<Product> getProductById(@PathVariable int id) {

        Product product = productService.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product/image/{id}")
    private ResponseEntity<byte[]> getImageByProductId(@PathVariable int id) {

        Product product = productService.getProductById(id);
        if(product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        byte[] imageFile = product.getImageData();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

    @GetMapping("product/search")
    private ResponseEntity<List<Product>> getProductsBySearch(@RequestParam String keyword) {
        List<Product> productList = productService.searchProducts(keyword);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @PostMapping("product")
    private ResponseEntity<?> addProduct(@RequestBody Product product) throws IOException {

        System.out.println("ProductController: addProduct");

        try {
            Product addedProduct = productService.addProduct(product);
            return new ResponseEntity<>(addedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id,
                                                 @RequestPart Product product,
                                                 @RequestPart MultipartFile imageFile) throws IOException {

            Product updatedProduct = productService.updateProduct(id, product, imageFile);
            if(updatedProduct != null) {
                return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
            }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {

        Product product = productService.getProductById(id);
        if (product != null) {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product is deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete", HttpStatus.NOT_FOUND);
        }
    }
}
