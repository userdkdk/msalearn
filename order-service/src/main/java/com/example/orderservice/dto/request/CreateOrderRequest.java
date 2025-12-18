package com.example.orderservice.dto.request;

import com.example.orderservice.domain.OrderEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String productId;
    private Integer qty;
    private Integer unitPrice;

    public OrderEntity toEntity(Integer userId, String orderId) {
        return new OrderEntity(productId,qty,unitPrice,userId,orderId);
    }
}
