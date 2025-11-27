package com.example.userservice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private User(
            final String email,
            final String name,
            final String passwordHash
    ) {
        validate(email, name, passwordHash);
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public static User ofHashed(String email, String name, String passwordHash) {
        return new User(email, name, passwordHash);
    }

    private void validate(String email, String name, String hash) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("email");
        if (hash == null || hash.isBlank()) throw new IllegalArgumentException("email");
    }

}
