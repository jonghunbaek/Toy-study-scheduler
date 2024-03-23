package toyproject.studyscheduler.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.member.entity.Role;

import static org.assertj.core.api.Assertions.*;

class JwtManagerTest {

    private JwtManager jwtManager;

    @BeforeEach
    void setUp() {
        jwtManager = new JwtManager(
            "NiOeyFbN1Gqo10bPgUyTFsRMkJpGLXSvGP04eFqj5B30r5TcrtlSXfQ7TndvYjNvfkEKLqILn0j1SmKODO6Yw3JpBBgI6nVPEbhqxeY1qbPSFGyzyEVxnl4bQcrnVneI",
            5,
            5,
            "test"
        );
    }

    @DisplayName("액세스토큰을 생성하고 검증한다.")
    @Test
    void createAccessToken() {
        // given
        long memberId = 1L;
        Role role = Role.ROLE_USER;
        String accessToken = jwtManager.createAccessToken(memberId, role);

        // when
        String[] subjects = jwtManager.parseAccessToken(accessToken);

        // then
        assertThat(subjects[0]).isEqualTo("1");
        assertThat(subjects[1]).isEqualTo("ROLE_USER");
    }

    @DisplayName("만료된 액세스 토큰을 파싱하면 예외가 발생한다.")
    @Test
    void parseAccessTokenWhenExpired() throws InterruptedException {
        // given
        long memberId = 1L;
        Role role = Role.ROLE_USER;
        String accessToken = jwtManager.createAccessToken(memberId, role);

        // when & then
        Thread.sleep(5000);
        assertThatThrownBy(() -> jwtManager.parseAccessToken(accessToken))
            .isInstanceOf(ExpiredJwtException.class);
    }

    @DisplayName("위조된 액세스 토큰을 파싱하면 예외가 발생한다.")
    @Test
    void parseAccessTokenWhenForgery() {
        // given
        String fakeToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IjM3MSIsImlhdCI6MTcwOTc4MDYwNCwic3ViIjoiMzcxIiwiZXhwIjoxNzEwOTkwMjA0fQ.YWgsDZ6N9KTyOF9w73PVuKMfHzU26tiXnJn8eRirkpo";

        // when & then
        assertThatThrownBy(() -> jwtManager.parseAccessToken(fakeToken))
            .isInstanceOf(SignatureException.class);
    }

    @DisplayName("토큰 재발행 전에 refresh token을 검증한다 - 만료 여부 확인")
    @Test
    void validateRefreshTokenWhenExpired() throws InterruptedException {
        // given
        String oldToken = jwtManager.createRefreshToken();

        // when & then
        Thread.sleep(5000);
        assertThatThrownBy(() -> jwtManager.validateRefreshToken(oldToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @DisplayName("토큰 재발행 전에 refresh token을 검증한다 - 위조 여부 확인")
    @Test
    void validateRefreshTokenWhenForgery() throws InterruptedException {
        // given
        String fakeToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IjM3MSIsImlhdCI6MTcwOTc4MDYwNCwic3ViIjoiMzcxIiwiZXhwIjoxNzEwOTkwMjA0fQ.YWgsDZ6N9KTyOF9w73PVuKMfHzU26tiXnJn8eRirkpo";

        // when & then
        Thread.sleep(5000);
        assertThatThrownBy(() -> jwtManager.validateRefreshToken(fakeToken))
                .isInstanceOf(SignatureException.class);
    }
}