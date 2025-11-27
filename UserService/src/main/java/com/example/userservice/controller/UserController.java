package com.example.userservice.controller;

import com.example.userservice.api.Greeting;
import com.example.userservice.dto.item.LoginResult;
import com.example.userservice.dto.request.CreateUserRequest;
import com.example.userservice.domain.UserRepository;
import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/user-service")
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final Environment env;
    private final Greeting greeting;

    @GetMapping("/health-check") // http://localhost:60000/health-check
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port"));
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request) {
        log.info("users.welcome ip: {}, {}, {}, {}", request.getRemoteAddr()
                , request.getRemoteHost(), request.getRequestURI(), request.getRequestURL());

//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        LoginResult loginResult = userService.login(loginRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + loginResult.getToken());
        return ResponseEntity.status(HttpStatus.OK).headers(headers)
                .build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer userId) {
        UserResponse response = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}


