package com.example.userservice.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResult {
    private String token;

    public static LoginResult of(String token) {
        return LoginResult.builder()
                .token(token)
                .build();
    }
}
