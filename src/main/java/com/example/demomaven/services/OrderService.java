package com.example.demomaven.services;

import com.example.demomaven.exceptions.BadRequestException;
import com.example.demomaven.exceptions.ResourceNotFoundException;
import com.example.demomaven.models.*;
import com.example.demomaven.models.dto.OrderTrackingResponse;
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + username));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot place order: Cart is empty!");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt((new Date()));
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
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }

    public List<Order> getUserOrders(String username) {
        Users user = usersRepository.findByUsername(username);
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public Order getOrderById(String username, int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to order details");
        }

        return order;
    }

    public List<Order> getAllOrdersForAdmin() {
        return orderRepository.findAll();
    }

    public OrderTrackingResponse getOrderTracking(String username, int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Ensure users can only track their own orders (unless ADMIN)
        if (!order.getUser().getUsername().equals(username) &&
                !order.getUser().getRole().name().equals("ROLE_ADMIN")) {
            throw new BadRequestException("Access denied: You cannot view this order.");
        }

        return new OrderTrackingResponse(
                order.getId(),
                order.getOrderStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    @Transactional
    public OrderTrackingResponse updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        OrderStatus currentStatus = order.getOrderStatus();

        // Prevent updates on terminal states
        if (currentStatus == OrderStatus.DELIVERED || currentStatus == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot change status of an order that is already " + currentStatus);
        }

        // Validate invalid transitions (e.g., cannot ship a cancelled order)
        if (newStatus == OrderStatus.DELIVERED && currentStatus != OrderStatus.SHIPPED) {
            throw new BadRequestException("Order must be SHIPPED before it can be marked DELIVERED.");
        }

        order.setOrderStatus(newStatus);
        order.setUpdatedAt(new Date());

        Order savedOrder = orderRepository.save(order);

        return new OrderTrackingResponse(
                savedOrder.getId(),
                savedOrder.getOrderStatus(),
                savedOrder.getTotalAmount(),
                savedOrder.getCreatedAt(),
                savedOrder.getUpdatedAt()
        );
    }

    @Transactional
    public OrderTrackingResponse cancelOrder(String username, int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getUser().getUsername().equals(username)) {
            throw new BadRequestException("Access denied: You can only cancel your own orders.");
        }

        if (order.getOrderStatus() != OrderStatus.PLACED) {
            throw new BadRequestException("Order can only be cancelled while in PLACED status.");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(new Date());

        Order savedOrder = orderRepository.save(order);

        return new OrderTrackingResponse(
                savedOrder.getId(),
                savedOrder.getOrderStatus(),
                savedOrder.getTotalAmount(),
                savedOrder.getCreatedAt(),
                savedOrder.getUpdatedAt()
        );
    }
}