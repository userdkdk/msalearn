package com.example.userservice.security.password;

public interface PasswordEncoderPort {
    String encode(String raw);
    boolean matches(String raw, String hash);
}
