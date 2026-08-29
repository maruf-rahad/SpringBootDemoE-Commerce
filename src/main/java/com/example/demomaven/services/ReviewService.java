package com.example.demomaven.services;

import com.example.demomaven.models.dto.ReviewRequest;
import com.example.demomaven.models.dto.ReviewResponse;
import com.example.demomaven.models.Product;
import com.example.demomaven.models.Review;
import com.example.demomaven.models.Users;
import com.example.demomaven.repositories.ProductRepository;
import com.example.demomaven.repositories.ReviewRepository;
import com.example.demomaven.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsersRepository usersRepository;

    public ReviewResponse addOrUpdateReview(String username, int productId, ReviewRequest request) {
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5.");
        }

        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Check if user has already reviewed this product; update if true
        Optional<Review> existingReview = reviewRepository.findByProductAndUser(product, user);

        Review review = existingReview.orElseGet(Review::new);
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(new Date());

        Review savedReview = reviewRepository.save(review);

        return new ReviewResponse(
                savedReview.getId(),
                user.getUsername(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getCreatedAt()
        );
    }

    public List<ReviewResponse> getProductReviews(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        return reviewRepository.findByProductOrderByCreatedAtDesc(product).stream()
                .map(r -> new ReviewResponse(
                        r.getId(),
                        r.getUser().getUsername(),
                        r.getRating(),
                        r.getComment(),
                        r.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public Double getAverageRating(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        Double avg = reviewRepository.getAverageRatingForProduct(product);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }
}