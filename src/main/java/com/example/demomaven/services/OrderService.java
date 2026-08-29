package com.example.demomaven.services;

import com.example.demomaven.models.*;
import com.example.demomaven.models.enums.OrderStatus;
import com.example.demomaven.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    public Order placeOrder(String username, String shippingAddress) {
        Users user = usersRepository.findByUsernameOptional(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + username));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot place order: Cart is empty!");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setOrderStatus(OrderStatus.PLACED); // Assign Enum value
        order.setShippingAddress(shippingAddress);

        long totalAmount = 0;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
            }

            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            if (product.getQuantity() == 0) {
                product.setAvailable(false);
            }
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getProductPrice());

            order.getOrderItems().add(orderItem);

            totalAmount += product.getProductPrice() * cartItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    @Transactional
    public Order updateOrderStatusByAdmin(int orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }

    public List<Order> getUserOrders(String username) {
        Users user = usersRepository.findByUsername(username);
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public Order getOrderById(String username, int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (!order.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to order details");
        }

        return order;
    }

    public List<Order> getAllOrdersForAdmin() {
        return orderRepository.findAll();
    }
}