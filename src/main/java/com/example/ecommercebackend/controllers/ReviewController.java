package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.models.dto.ReviewRequest;
import com.example.ecommercebackend.models.dto.ReviewResponse;
import com.example.ecommercebackend.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/{productId}/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Public Endpoint: Read reviews for a product
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable int productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }

    // Public Endpoint: Get average rating for a product
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable int productId) {
        return ResponseEntity.ok(reviewService.getAverageRating(productId));
    }

    // Authenticated Endpoint: Post or update a review
    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(
            Authentication authentication,
            @PathVariable int productId,
            @RequestBody ReviewRequest request) {
        String username = authentication.getName();
        return ResponseEntity.ok(reviewService.addOrUpdateReview(username, productId, request));
    }
}