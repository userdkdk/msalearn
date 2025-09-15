package com.example.apigatewayservice.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@Getter
@RequiredArgsConstructor
public class JWTKeyService {

    private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.ES256;
    private JWSKeySelector<SecurityContext> publicJwsKeySelector;
    private JWKSource<SecurityContext> privateJwkSource;

    public void loadKey() throws IOException {
        String publicKeyString = readFile("keys/ec256-public.pem");

        JWKSource<SecurityContext> publicJWKSource = parseKey(publicKeyString);

        this.publicJwsKeySelector = createJWSKeySelector(publicJWKSource);
    }

    private JWSKeySelector<SecurityContext> createJWSKeySelector(JWKSource<SecurityContext> jwkSource) {
        try {
            return JWSAlgorithmFamilyJWSKeySelector.fromJWKSource(jwkSource);
        } catch (KeySourceException e) {
            throw new IllegalArgumentException("Failed to create JWSKeySelector.", e);
        }
    }

    private JWKSource<SecurityContext> parseKey(String keyString) {
        JWK jwk;
        try {
            jwk = ECKey.parseFromPEMEncodedObjects(keyString);
        } catch (JOSEException e) {
            throw new IllegalArgumentException("Invalid PEM Key: ",e);
        }
        return new ImmutableJWKSet<>(new JWKSet(jwk));
    }

    private String readFile(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        try (
                Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(reader);
             ) {
            StringBuilder s = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line !=null) {
                s.append(line).append(System.lineSeparator());
                line = bufferedReader.readLine();
            }
            return s.toString();
        } catch (IOException e) {
            log.error("Failed to read file: '{}'",path,e);
            throw e;
        }
    }
}
