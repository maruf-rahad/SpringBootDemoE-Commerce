package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.models.Order;
import com.example.ecommercebackend.models.dto.OrderTrackingResponse;
import com.example.ecommercebackend.models.dto.UpdateOrderStatusRequest;
import com.example.ecommercebackend.models.enums.OrderStatus;
import com.example.ecommercebackend.services.OrderService;
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

    // --- Customer Endpoints ---

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(
            Authentication authentication,
            @RequestParam String shippingAddress) {
        String username = authentication.getName();
        Order order = orderService.placeOrder(username, shippingAddress);
        return ResponseEntity.ok(order);
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

    // --- Admin Endpoints ---

    @GetMapping("/admin/all")
    public ResponseEntity<List<Order>> getAllOrdersForAdmin() {
        return ResponseEntity.ok(orderService.getAllOrdersForAdmin());
    }

    @PutMapping("/admin/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable int orderId,
            @RequestParam OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatusByAdmin(orderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Customer: Track specific order
    @GetMapping("/{orderId}/track")
    public ResponseEntity<OrderTrackingResponse> trackOrder(
            Authentication authentication,
            @PathVariable int orderId) {
        return ResponseEntity.ok(orderService.getOrderTracking(authentication.getName(), orderId));
    }

    // Customer: Cancel an order before processing
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderTrackingResponse> cancelOrder(
            Authentication authentication,
            @PathVariable int orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(authentication.getName(), orderId));
    }

    // Admin Only: Update order status workflow
    @PutMapping("/admin/{orderId}/status")
    public ResponseEntity<OrderTrackingResponse> updateOrderStatus(
            @PathVariable int orderId,
            @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.getStatus()));
    }
}