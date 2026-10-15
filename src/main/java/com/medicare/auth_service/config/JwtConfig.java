package com.medicare.auth_service.config;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
@Configuration
public class JwtConfig {
    private final RSAKey rsaKey;
    public JwtConfig() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }
    @Bean public JWKSource<SecurityContext> jwkSource() {
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }
    @Bean public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) { return new NimbusJwtEncoder(jwkSource); }
    @Bean public JwtDecoder jwtDecoder() {
        try { return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build(); } 
        catch (Exception e) { throw new RuntimeException(e); }
    }
    @Bean public JWKSet jwkSet() { return new JWKSet(rsaKey); }
}
