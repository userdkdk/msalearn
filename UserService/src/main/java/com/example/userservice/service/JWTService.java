package com.example.userservice.service;

import com.example.userservice.jpa.UserEntity;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.mint.ConfigurableJWSMinter;
import com.nimbusds.jose.mint.DefaultJWSMinter;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JWTService {

    private static final String CLAIM_TYPE = "type";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";
    private static final String JWT_ISSUER = "projdkdk";
    private static final int ACCESS_EXPIRES_IN_SECONDS = 5 * 60;
    private static final int REFRESH_EXPIRES_IN_SECONDS = 60 * 60;

    private final JWTKeyService jwtKeyService;

    @Getter
    // 키 검증용
    private final ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
    // 키 생성용
    private final ConfigurableJWSMinter<SecurityContext> jwsMinter = new DefaultJWSMinter<>();

    @PostConstruct
    void refreshKeys() throws IOException {
        this.jwtKeyService.loadKey();
        this.jwtProcessor.setJWSKeySelector(this.jwtKeyService.getPublicJwsKeySelector());
        this.jwsMinter.setJWKSource(this.jwtKeyService.getPrivateJwkSource());
    }

    public String generateAccessToken(String userId) throws JOSEException {
        JWSHeader header = new JWSHeader.Builder(this.jwtKeyService.getJwsAlgorithm())
                .type(JOSEObjectType.JWT)
                .build();

        Instant expiresAt = Instant.now()
                .plusSeconds(ACCESS_EXPIRES_IN_SECONDS)
                .truncatedTo(ChronoUnit.SECONDS);

        Payload payload = new JWTClaimsSet.Builder()
                .issuer(JWT_ISSUER)
                .subject(String.valueOf(userId))
                .expirationTime(Date.from(expiresAt))
                .claim(CLAIM_TYPE, TYPE_ACCESS)
                .build()
                .toPayload();

        return this.jwsMinter.mint(header, payload, null).serialize();
    }

    public String generateRefreshToken(UserEntity user) throws JOSEException {
        JWSHeader header = new JWSHeader.Builder(this.jwtKeyService.getJwsAlgorithm())
                .type(JOSEObjectType.JWT)
                .build();

        Instant expiresAt = Instant.now()
                .plusSeconds(REFRESH_EXPIRES_IN_SECONDS)
                .truncatedTo(ChronoUnit.SECONDS);

        Payload payload = new JWTClaimsSet.Builder()
                .issuer(JWT_ISSUER)
                .subject(String.valueOf(user.getId()))
                .expirationTime(Date.from(expiresAt))
                .claim(CLAIM_TYPE, TYPE_REFRESH)
                .build()
                .toPayload();

        return this.jwsMinter.mint(header, payload, null).serialize();
    }
}