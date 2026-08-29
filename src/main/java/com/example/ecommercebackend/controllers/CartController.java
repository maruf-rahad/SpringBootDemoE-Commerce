package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.models.Cart;
import com.example.ecommercebackend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.getCartByUser(username));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            Authentication authentication,
            @RequestParam int productId,
            @RequestParam(defaultValue = "1") int quantity) {
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.addToCart(username, productId, quantity));
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<Cart> updateItemQuantity(
            Authentication authentication,
            @PathVariable int itemId,
            @RequestParam int quantity) {
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.updateItemQuantity(username, itemId, quantity));
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Cart> removeItem(
            Authentication authentication,
            @PathVariable int itemId) {
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.removeItemFromCart(username, itemId));
    }
}