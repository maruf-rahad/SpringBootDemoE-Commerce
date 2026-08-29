package com.example.demomaven.services;

import com.example.demomaven.models.Cart;
import com.example.demomaven.models.CartItem;
import com.example.demomaven.models.Product;
import com.example.demomaven.models.Users;
import com.example.demomaven.repositories.CartItemRepository;
import com.example.demomaven.repositories.CartRepository;
import com.example.demomaven.repositories.ProductRepository;
import com.example.demomaven.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Cart getCartByUser(String username) {
        Users user = usersRepository.findByUsername(username);
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public Cart addToCart(String username, int productId, int quantity) {
        Cart cart = getCartByUser(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    public Cart updateItemQuantity(String username, int itemId, int quantity) {
        Cart cart = getCartByUser(username);

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
        }

        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(String username, int itemId) {
        Cart cart = getCartByUser(username);
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return cartRepository.save(cart);
    }
}