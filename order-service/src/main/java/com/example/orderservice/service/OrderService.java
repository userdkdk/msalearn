package com.example.orderservice.service;

import com.example.orderservice.api.ResponseOrder;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderMapper;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    public ResponseOrder createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());
        OrderEntity orderEntity = orderMapper.toEntity(orderDto);

        orderRepository.save(orderEntity);

        ResponseOrder res = orderMapper.toResponse(orderEntity);
        return res;
    }

    public List<ResponseOrder> getOrdersByUserId(String userId) {
        Iterable<OrderEntity> res = orderRepository.findByUserId(userId);
        return orderMapper.toResponseList(res);
    }
}
