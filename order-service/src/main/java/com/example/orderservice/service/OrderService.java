package com.example.orderservice.service;

import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderRepository;
import com.example.orderservice.infrastructure.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;

    public OrderResponse createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        String orderId = UUID.randomUUID().toString();
        Order order = createOrderRequest.toEntity(userId, orderId);

        orderRepository.save(order);
        log.info(order.getId()+" a");
        log.info("-0---------------");
        // kafka 보내기
        kafkaProducer.send("example-catalog-topic", order);
        return OrderResponse.of(order);
    }

    public List<OrderResponse> getOrdersByUserId(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(OrderResponse::of)
                .toList();
    }
}
