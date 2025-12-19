package com.example.orderservice.infrastructure;

import com.example.orderservice.domain.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItem {
    private Integer id;
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer userId;
    private Integer totalPrice;

    public static OrderItem of(Order order) {
        return OrderItem.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .qty(order.getQty())
                .unitPrice(order.getUnitPrice())
                .userId(order.getUserId())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
