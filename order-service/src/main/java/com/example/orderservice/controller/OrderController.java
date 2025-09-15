package com.example.orderservice.controller;

import com.example.orderservice.api.RequestOrder;
import com.example.orderservice.api.ResponseOrder;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderMapper;
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
    OrderMapper orderMapper;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                     @RequestBody RequestOrder requestOrder) {
        log.info("before order");
        OrderDto dto = orderMapper.fromRequest(requestOrder);
        dto.setUserId(userId);
        ResponseOrder createOrder = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {
        log.info("Before retrieve orders data");
        List<ResponseOrder> res = orderService.getOrdersByUserId(userId);

        log.info("Add retrieved orders data");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
