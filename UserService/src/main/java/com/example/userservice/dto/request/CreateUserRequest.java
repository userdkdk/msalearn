package com.example.userservice.dto.request;

import com.example.userservice.dto.item.ResponseOrder;
import com.example.userservice.domain.User;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class CreateUserRequest {
    private String email;
    private String name;
    private String password;

    private List<ResponseOrder> orders;

    public User toEntity(String hash) {
        return User.ofHashed(email, name, hash);
    }
}
