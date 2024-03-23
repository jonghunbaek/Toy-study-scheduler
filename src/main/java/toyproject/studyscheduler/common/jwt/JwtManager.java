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
public class JwtManager {
    public static final String SUBJECT_DELIMITER = ":";

    private long accessExpiration;
    private long refreshExpiration;
    private String issuer;
    private SecretKey secretKey;

    public JwtManager(
        @Value("${secret-key}") String secretKey,
        @Value("${access-expiration-hours}") long accessExpiration,
        @Value("${refresh-expiration-hours}") long refreshExpiration,
        @Value("${issuer}") String issuer) {

        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.issuer = issuer;
    }

    public String createAccessToken(Long memberId, Role role) {
        String subject = createSubject(memberId, role);
        return createToken(subject, secretKey, accessExpiration);
    }

    private String createSubject(Long memberId, Role role) {
        return memberId + SUBJECT_DELIMITER + role.toString();
    }

    public String createRefreshToken() {
        return createToken("", secretKey, refreshExpiration);
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
        JwtParser jwtParser = createJwtParser(secretKey);

        return parseToken(token, jwtParser)
            .split(SUBJECT_DELIMITER);
    }

    public void validateRefreshToken(String refreshToken) {
        JwtParser jwtParser = createJwtParser(secretKey);

        parseToken(refreshToken, jwtParser);
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
