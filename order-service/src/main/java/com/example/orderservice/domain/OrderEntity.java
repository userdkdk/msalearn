package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name="orders")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false, length = 120)
    private String productId;
    @Column(nullable = false)
    private Integer qty;
    @Column(nullable = false)
    private Integer unitPrice;
    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private Integer userId;
    @Column(nullable = false, unique = true)
    private String orderId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public OrderEntity(String productId, Integer qty, Integer unitPrice, Integer userId,
                       String orderId) {
        this.productId = productId;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.userId = userId;
        this.totalPrice = calculateSum(qty,unitPrice);
        this.orderId = orderId;
    }

    private Integer calculateSum(Integer qty, Integer unitPrice) {
        return qty * unitPrice;
    }
}
