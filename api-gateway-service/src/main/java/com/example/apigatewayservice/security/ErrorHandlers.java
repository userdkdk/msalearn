package com.example.apigatewayservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

@Configuration
class ErrorHandlers {

    @Bean
    ServerAuthenticationEntryPoint authenticationEntryPoint() {
        // 인증 실패(토큰 없음/깨짐/만료) → 401
        return (exchange, ex) -> {
            var res = exchange.getResponse();
            res.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            res.getHeaders().setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            var body = """
                {"error":"unauthorized","message":"Invalid or missing access token"}
                """.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            return res.writeWith(reactor.core.publisher.Mono.just(res.bufferFactory().wrap(body)));
        };
    }

    @Bean
    ServerAccessDeniedHandler accessDeniedHandler() {
        // 인증은 되었으나 권한 부족 → 403
        return (exchange, ex) -> {
            var res = exchange.getResponse();
            res.setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
            res.getHeaders().setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            var body = """
                {"error":"forbidden","message":"Insufficient permissions"}
                """.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            return res.writeWith(reactor.core.publisher.Mono.just(res.bufferFactory().wrap(body)));
        };
    }
}

