package com.example.userservice.dto.response;

import com.example.userservice.dto.item.ResponseOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserResponse {
    private String email;
    private String name;
    private Integer userId;
    private List<ResponseOrder> orders;

    public static UserResponse of(String email, String name, Integer userId, List<ResponseOrder> orders) {
        return UserResponse.builder()
                .email(email)
                .name(name)
                .userId(userId)
                .orders(orders)
                .build();
    }
}
