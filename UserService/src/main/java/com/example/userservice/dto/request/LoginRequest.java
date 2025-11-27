package com.example.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    @NotBlank(message="이메일입력")
    private String email;
    @NotBlank(message="비밀번호 입력")
    private String password;
}
