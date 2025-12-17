package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.item.LoginResult;
import com.example.userservice.dto.item.ResponseOrder;
import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.security.jwt.JwtTokenProvider;
import com.example.userservice.security.password.PasswordEncoderPort;
import com.example.userservice.dto.request.CreateUserRequest;
import com.example.userservice.domain.User;
import com.example.userservice.domain.UserRepository;
import com.example.userservice.service.dto.OrderResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final Environment env;
    private final UserRepository userRepository;
    private final PasswordEncoderPort encoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;

    @Transactional
    public void createUser(CreateUserRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new CannotCreateTransactionException("email");
        }
        String hash = encoder.encode(req.getPassword());
        User user = req.toEntity(hash);
        userRepository.save(user);
    }

    public LoginResult login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CannotCreateTransactionException("email"));

        // 비밀번호 검증
        if (!encoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException();
        }

        // JWT 토큰 생성
        String token = jwtTokenProvider.createAccessToken(String.valueOf(user.getId()));

        return LoginResult.of(token);
    }


    public UserResponse getUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CannotCreateTransactionException("userId"));
        String orderUrl = String.format(env.getProperty("order-service.url"), userId);
//        ResponseEntity<List<OrderResponse>> orderListResponse =
//                restTemplate.exchange(orderUrl, HttpMethod.GET,null,
//                        new ParameterizedTypeReference<List<OrderResponse>>() {});
//        List<OrderResponse> orderList = orderListResponse.getBody();
        List<OrderResponse> orderList = null;
        try {
            orderList = orderServiceClient.getOrders(userId);
        } catch (FeignException e) {
            log.error(e.getMessage());
        }

        return UserResponse.of(user.getEmail(),user.getName(),user.getId(),orderList);
    }
}
