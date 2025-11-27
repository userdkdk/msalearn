package com.example.userservice.security.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class UserPrinciple {
    private Integer id;

    public static UserPrinciple of(Integer id) {
        return UserPrinciple.builder()
                .id(id)
                .build();
    }
}
