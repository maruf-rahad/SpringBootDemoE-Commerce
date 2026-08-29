package com.example.ecommercebackend.models.dto;

import com.example.ecommercebackend.models.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTrackingResponse {
    private int orderId;
    private OrderStatus status;
    private double totalAmount;
    private Date createdAt;
    private Date updatedAt;
}