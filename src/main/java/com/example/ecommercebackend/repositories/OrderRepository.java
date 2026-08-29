package com.example.ecommercebackend.repositories;

import com.example.ecommercebackend.models.Order;
import com.example.ecommercebackend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByOrderDateDesc(Users user);
}