package com.example.orderservice.controller;

import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order-service")
@AllArgsConstructor
public class OrderController {
    OrderService orderService;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable("userId") Integer userId,
                                                     @RequestBody CreateOrderRequest createOrderRequest) {
        log.info("before order");
        OrderResponse createOrder = orderService.createOrder(userId, createOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponse>> getOrder(@PathVariable("userId") Integer userId) {
        log.info("Before retrieve orders data");
        List<OrderResponse> res = orderService.getOrdersByUserId(userId);

        log.info("Add retrieved orders data");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
