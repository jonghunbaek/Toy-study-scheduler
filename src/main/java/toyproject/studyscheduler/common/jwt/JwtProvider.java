package toyproject.studyscheduler.common.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtProvider {
    private long accessExpiration;
    private long refreshExpiration;
    private String issuer;
    private SecretKey accessSecretKey;

    public JwtProvider(
        @Value("${access-secret-key}") String accessSecretKey,
        @Value("${access-expiration-hours}") long accessExpiration,
        @Value("${refresh-expiration-hours}") long refreshExpiration,
        @Value("${issuer}") String issuer) {

        this.accessSecretKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes());
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.issuer = issuer;
    }

    public String[] parseAccessToken(String accessToken) {
        return null;
    }
}
