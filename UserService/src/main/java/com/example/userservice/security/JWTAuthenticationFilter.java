package com.example.userservice.security;

import com.example.userservice.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private static final String CLAIM_TYPE = "type";
    private static final String TYPE_ACCESS = "access";
    private static final String COOKIE_NAME = "PJT_TOKEN";

    private final JwtDecoder jwtDecoder;
    private final UserService userService;

    private static final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Jwt jwt;

        try {
            jwt = jwtDecoder.decode(token);
        } catch (JwtException e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!Objects.equals(jwt.getClaim(CLAIM_TYPE), TYPE_ACCESS)) {
            filterChain.doFilter(request, response);
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(jwt.getSubject());
        } catch (NumberFormatException e) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userService.loadUserById(userId).orElse(null);
        if (userDetails == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(userDetails, null, List.of());

        SecurityContext securityContext = securityContextHolderStrategy.createEmptyContext();
        securityContext.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(securityContext);

        try {
            filterChain.doFilter(request, response);
        } finally {
            securityContextHolderStrategy.clearContext();
        }
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {                 // ★ null 가드
            for (Cookie c : cookies) {
                if (COOKIE_NAME.equals(c.getName())) {
                    return c.getValue();
                }
            }
        }

        return null;
    }
}
