package com.example.demomaven.models.dto;

import com.example.demomaven.models.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}