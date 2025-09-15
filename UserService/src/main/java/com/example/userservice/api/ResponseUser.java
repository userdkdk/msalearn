package com.example.userservice.api;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {
    private String email;

    private String name;

    private String userId;
    private List<ResponseOrder> orders;
}
