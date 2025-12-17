package com.example.userservice.dto.response;

import com.example.userservice.dto.item.ResponseOrder;
import com.example.userservice.service.dto.OrderResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserResponse {
    private String email;
    private String name;
    private Integer userId;
    private List<OrderResponse> orders;

    public static UserResponse of(String email, String name, Integer userId, List<OrderResponse> orders) {
        return UserResponse.builder()
                .email(email)
                .name(name)
                .userId(userId)
                .orders(orders)
                .build();
    }
}
