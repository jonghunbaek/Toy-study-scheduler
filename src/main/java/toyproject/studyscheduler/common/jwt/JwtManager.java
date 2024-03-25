package toyproject.studyscheduler.common.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import toyproject.studyscheduler.member.entity.Role;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

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
        String subject = createSubject(memberId, role.toString());
        return createToken(subject, accessExpiration);
    }

    private String createSubject(Long memberId, String role) {
        return memberId + SUBJECT_DELIMITER + role;
    }

    public String createRefreshToken() {
        return createToken("", refreshExpiration);
    }

    private String createToken(String subject, long expiration) {
        return Jwts.builder()
            .signWith(secretKey, Jwts.SIG.HS512)
            .subject(subject)
            .issuer(issuer)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(expiration, ChronoUnit.SECONDS)))
            .compact();
    }

    public String[] parseAccessToken(String token) {
        JwtParser jwtParser = createJwtParser();

        return parseToken(token, jwtParser)
            .getSubject()
            .split(SUBJECT_DELIMITER);
    }

    public void validateRefreshToken(String refreshToken) {
        JwtParser jwtParser = createJwtParser();

        parseToken(refreshToken, jwtParser);
    }

    private JwtParser createJwtParser() {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build();
    }

    private Claims parseToken(String token, JwtParser jwtParser) {
        return jwtParser.parseSignedClaims(token)
            .getPayload();
    }

    public String reissueAccessToken(String accessTokens) {
        String[] subjects = decodeJwtPayload(accessTokens);

        return createToken(createSubject(Long.parseLong(subjects[0]), subjects[1]), accessExpiration);
    }

    /**
     * 만료된 토큰을 jwtParser로 파싱하게 되면 만료 예외가 발생하므로 토큰의 subject부분만 디코딩
     * @param oldAccessToken 만료된 토큰
     * @return
     */
    private String[] decodeJwtPayload(String oldAccessToken) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]), StandardCharsets.UTF_8), Map.class)
                    .get("sub")
                    .toString()
                    .split(SUBJECT_DELIMITER);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public long calculateExpirationSec(String accessToken, Instant now) {
        Date expiration = getExpiration(accessToken);

        return Duration.between(now, expiration.toInstant())
            .getSeconds();
    }

    private Date getExpiration(String accessToken) {
        JwtParser jwtParser = createJwtParser();

        return parseToken(accessToken, jwtParser)
            .getExpiration();
    }
}
