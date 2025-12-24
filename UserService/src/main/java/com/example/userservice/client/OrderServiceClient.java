package com.example.userservice.client;

import com.example.userservice.exception.FeignDecoderError;
import com.example.userservice.service.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="order-service", configuration = FeignDecoderError.class, url = "${order-service-url}")
public interface OrderServiceClient {

    @GetMapping("/order-service/{userId}/orders")
    List<OrderResponse> getOrders(@PathVariable Integer userId);
}
