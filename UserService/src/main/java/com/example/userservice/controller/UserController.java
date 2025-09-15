package com.example.userservice.controller;

import com.example.userservice.api.Greeting;
import com.example.userservice.api.RequestUser;
import com.example.userservice.api.ResponseUser;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserMapper;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/user-service")
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final Environment env;
    private final UserRepository userRepository;
    private final Greeting greeting;
    private final UserMapper userMapper;

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
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {

        UserDto userDto = userMapper.fromRequest(user);
        ResponseUser responseUser = userService.createUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        List<ResponseUser> userList = userService.getUserByAll();

        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        ResponseUser userDto = userService.getUserByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

}


