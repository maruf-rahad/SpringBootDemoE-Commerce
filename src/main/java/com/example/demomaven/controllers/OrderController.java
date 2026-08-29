package com.example.demomaven.controllers;

import com.example.demomaven.models.Order;
import com.example.demomaven.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            Authentication authentication,
            @RequestParam String shippingAddress) {
        try {
            String username = authentication.getName();
            Order order = orderService.placeOrder(username, shippingAddress);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(orderService.getUserOrders(username));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetails(
            Authentication authentication,
            @PathVariable int orderId) {
        try {
            String username = authentication.getName();
            return ResponseEntity.ok(orderService.getOrderById(username, orderId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}