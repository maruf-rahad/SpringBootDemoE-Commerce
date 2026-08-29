package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.models.Product;
import com.example.ecommercebackend.models.Review;
import com.example.ecommercebackend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByProductOrderByCreatedAtDesc(Product product);

    Optional<Review> findByProductAndUser(Product product, Users user);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product = :product")
    Double getAverageRatingForProduct(Product product);
}