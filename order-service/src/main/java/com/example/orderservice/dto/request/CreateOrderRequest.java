package com.example.orderservice.dto.request;

import com.example.orderservice.domain.Order;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private String productId;
    private Integer qty;
    private Integer unitPrice;

    public Order toEntity(Integer userId, String orderId) {

        return Order.create(productId,qty,unitPrice,userId,orderId);
    }
}
