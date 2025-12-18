package com.example.orderservice.dto.response;

import com.example.orderservice.domain.OrderEntity;
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

    public static OrderResponse of(OrderEntity orderEntity) {
        return OrderResponse.builder()
                .productId(orderEntity.getProductId())
                .qty(orderEntity.getQty())
                .orderId(orderEntity.getOrderId())
                .unitPrice(orderEntity.getUnitPrice())
                .totalPrice(orderEntity.getTotalPrice())
                .userId(orderEntity.getUserId())
                .build();
    }
}
