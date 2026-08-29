package com.example.ecommercebackend.models.dto;

import com.example.ecommercebackend.models.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}