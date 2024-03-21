package toyproject.studyscheduler.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.member.entity.Role;

import static org.assertj.core.api.Assertions.*;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(
            "NiOeyFbN1Gqo10bPgUyTFsRMkJpGLXSvGP04eFqj5B30r5TcrtlSXfQ7TndvYjNvfkEKLqILn0j1SmKODO6Yw3JpBBgI6nVPEbhqxeY1qbPSFGyzyEVxnl4bQcrnVneI",
            5,
            20,
            "test"
        );
    }

    @DisplayName("액세스토큰을 생성하고 검증한다.")
    @Test
    void createAccessToken() {
        // given
        String email = "hong@gmail.com";
        Role role = Role.ROLE_USER;
        String accessToken = jwtProvider.createAccessToken(email, role);

        // when
        String[] subjects = jwtProvider.parseAccessToken(accessToken);

        // then
        assertThat(subjects[0]).isEqualTo("hong@gmail.com");
        assertThat(subjects[1]).isEqualTo("ROLE_USER");
    }

    @DisplayName("만료된 액세스 토큰을 파싱하면 예외가 발생한다.")
    @Test
    void parseAccessTokenWhenExpired() throws InterruptedException {
        // given
        String email = "hong@gmail.com";
        Role role = Role.ROLE_USER;
        String accessToken = jwtProvider.createAccessToken(email, role);

        // when & then
        Thread.sleep(5000);
        assertThatThrownBy(() -> jwtProvider.parseAccessToken(accessToken))
            .isInstanceOf(ExpiredJwtException.class);
    }

    @DisplayName("위조된 액세스 토큰을 파싱하면 예외가 발생한다.")
    @Test
    void parseAccessTokenWhenforgery() {
        // given
        String fakeToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IjM3MSIsImlhdCI6MTcwOTc4MDYwNCwic3ViIjoiMzcxIiwiZXhwIjoxNzEwOTkwMjA0fQ.YWgsDZ6N9KTyOF9w73PVuKMfHzU26tiXnJn8eRirkpo";

        // when & then
        assertThatThrownBy(() -> jwtProvider.parseAccessToken(fakeToken))
            .isInstanceOf(SignatureException.class);
    }
}