package com.example.userservice.security;

import com.example.userservice.api.RequestLogin;
import com.example.userservice.dto.UserDto;
import com.example.userservice.service.JWTService;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private Environment environment;
    private final JWTService jwtService;

    public AuthenticationFilter(UserService userService, Environment environment,
                                     JWTService jwtService,
                                     AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.environment = environment;
        this.jwtService = jwtService;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            log.info("start authenticate");
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            log.info("get authenticate");
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("success in");
        var principal = (User) authResult.getPrincipal();
        int userId = Integer.parseInt(principal.getUsername());
        UserDto userDetails = userService.getUserDetailsById(userId);
        log.info(String.valueOf(userDetails));
        String token = null;
        try {
            token = jwtService.generateAccessToken(userDetails.getUserId());

            Cookie cookie = new Cookie("PJT_TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            // 배포에선 samesite 켜기

            res.addCookie(cookie);
            log.info("Access token = {}", token);
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write("{\"message\":\"login success\"}");
            res.getWriter().flush();

        } catch (JOSEException e) {
            unsuccessfulAuthentication(req, res, new InternalAuthenticationServiceException("JWT error", e));
        }
    }
}
