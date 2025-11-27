package com.example.orderservice.service;

import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {
    OrderRepository orderRepository;

    public OrderResponse createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        String orderId = UUID.randomUUID().toString();
        OrderEntity order = createOrderRequest.toEntity(userId, orderId);

        orderRepository.save(order);

        return OrderResponse.of(order);
    }

    public List<OrderResponse> getOrdersByUserId(Integer userId) {
        List<OrderEntity> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(OrderResponse::of)
                .toList();
    }
}
