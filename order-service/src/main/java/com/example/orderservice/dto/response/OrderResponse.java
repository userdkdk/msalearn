package com.example.orderservice.dto.response;

import com.example.orderservice.domain.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId;
    private Integer userId;

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .productId(order.getProductId())
                .qty(order.getQty())
                .orderId(order.getOrderId())
                .unitPrice(order.getUnitPrice())
                .totalPrice(order.getTotalPrice())
                .userId(order.getUserId())
                .build();
    }
}
