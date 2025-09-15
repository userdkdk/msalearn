package com.example.apigatewayservice.service;

import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JWTService {
    private final JWTKeyService jwtKeyService;

    @Getter
    // 키 검증용
    private final ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

    @PostConstruct
    void refreshKeys() throws IOException {
        this.jwtKeyService.loadKey();
        this.jwtProcessor.setJWSKeySelector(this.jwtKeyService.getPublicJwsKeySelector());
    }
}