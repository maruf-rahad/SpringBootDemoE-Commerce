package com.example.demomaven.repositories;

import com.example.demomaven.models.Order;
import com.example.demomaven.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserOrderByOrderDateDesc(Users user);
}