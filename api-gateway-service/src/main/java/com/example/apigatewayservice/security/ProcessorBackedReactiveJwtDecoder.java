package com.example.apigatewayservice.security;

import com.example.apigatewayservice.service.JWTService;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProcessorBackedReactiveJwtDecoder implements ReactiveJwtDecoder {
    private final JWTService jwtService; // 네가 가진 DefaultJWTProcessor 제공

    @Override
    public Mono<Jwt> decode(String token) {
        return Mono.fromCallable(() -> {
                    var claims = jwtService.getJwtProcessor().process(token, (SecurityContext) null);
                    Instant iat = claims.getIssueTime() != null ? claims.getIssueTime().toInstant() : null;
                    Instant exp = claims.getExpirationTime() != null ? claims.getExpirationTime().toInstant() : null;
                    return new Jwt(token, iat, exp, Map.of(), claims.getClaims());
                })
                // Nimbus Processor는 블로킹이므로 별 스레드에서
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(ex -> new JwtException("Invalid JWT", ex)); // → 401로 매핑됨
    }
}
