package toyproject.studyscheduler.common.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import toyproject.studyscheduler.member.entity.Role;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {
    public static final String SUBJECT_DELIMITER = ":";
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

    public String createAccessToken(String email, Role role) {
        String subject = createSubject(email, role);
        return createToken(subject, accessSecretKey, accessExpiration);
    }

    private String createSubject(String email, Role role) {
        return email + SUBJECT_DELIMITER + role.toString();
    }

    private String createToken(String subject, SecretKey secretKey, long expiration) {
        return Jwts.builder()
            .signWith(secretKey, Jwts.SIG.HS512)
            .subject(subject)
            .issuer(issuer)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(expiration, ChronoUnit.SECONDS)))
            .compact();
    }

    public String[] parseAccessToken(String token) {
        JwtParser jwtParser = createJwtParser(accessSecretKey);

        return parseToken(token, jwtParser)
            .split(SUBJECT_DELIMITER);
    }

    private JwtParser createJwtParser(SecretKey secretKey) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build();
    }

    private String parseToken(String token, JwtParser jwtParser) {
        return jwtParser.parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
