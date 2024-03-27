package toyproject.studyscheduler.token.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.common.response.ResponseCode;
import toyproject.studyscheduler.common.jwt.JwtManager;
import toyproject.studyscheduler.member.entity.Role;
import toyproject.studyscheduler.token.application.dto.TokenCreationInfo;
import toyproject.studyscheduler.token.entity.BlackToken;
import toyproject.studyscheduler.token.entity.RefreshToken;
import toyproject.studyscheduler.token.exception.TokenException;
import toyproject.studyscheduler.token.repository.RefreshTokenRepository;
import toyproject.studyscheduler.token.repository.redis.BlackTokenRepository;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class TokenServiceTest {

    @Autowired
    TokenService tokenService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    BlackTokenRepository blackTokenRepository;
    @Autowired
    JwtManager jwtManager;

    @DisplayName("access, refresh token을 생성할 때 refresh token을 DB에 저장한다.")
    @Test
    void saveRefreshTokenWhenCreateToken() {
        // given
        TokenCreationInfo info = TokenCreationInfo.builder()
            .memberId(1L)
            .role(Role.ROLE_USER)
            .build();

        // when
        Tokens tokens = tokenService.createTokens(info);
        RefreshToken refreshToken = refreshTokenRepository.findById(1L)
            .orElseThrow();

        // then
        assertThat(tokens.getRefreshToken()).isEqualTo(refreshToken.getToken());
    }

    @DisplayName("refresh token이 이미 DB에 존재하면 새로운 토큰으로 갱신한다.")
    @Test
    void saveRefreshTokenWhenExist() {
        // given
        TokenCreationInfo info = TokenCreationInfo.builder()
            .memberId(1L)
            .role(Role.ROLE_USER)
            .build();

        // when
        Tokens oldTokens = tokenService.createTokens(info);

        // 일시정지 하지 않으면 시간 차이가 없어 결과적으로 같은 refresh token이 생성됨
        sleep(1000);
        Tokens newTokens = tokenService.createTokens(info);

        RefreshToken refreshToken = refreshTokenRepository.findById(1L)
            .orElseThrow();

        // then
        assertThat(oldTokens.getRefreshToken()).isNotEqualTo(refreshToken.getToken());
        assertThat(newTokens.getRefreshToken()).isEqualTo(refreshToken.getToken());
    }

    @DisplayName("acces token이 만료되면 access, refresh token을 재발행한다.")
    @Test
    void reissuTokens() {
        // given
        TokenCreationInfo info = TokenCreationInfo.builder()
                .memberId(1L)
                .role(Role.ROLE_USER)
                .build();

        // when
        Tokens oldTokens = tokenService.createTokens(info);
        sleep(5000);

        // when
        Tokens newTokens = tokenService.reissueTokens(oldTokens.getAccessToken(), oldTokens.getRefreshToken());

        // then
        assertThat(newTokens.getAccessToken()).isNotEqualTo(oldTokens.getAccessToken());
        assertThat(newTokens.getRefreshToken()).isNotEqualTo(oldTokens.getRefreshToken());
    }

    @DisplayName("access, refresh token을 재발행할 때 DB에 저장된 refresh token이 다를 경우 예외가 발생한다.")
    @Test
    void reissuTokensWhenRefreshTokenNotSame() {
        // given
        TokenCreationInfo info = TokenCreationInfo.builder()
                .memberId(1L)
                .role(Role.ROLE_USER)
                .build();

        // when
        Tokens oldTokens = tokenService.createTokens(info);
        RefreshToken refreshToken = refreshTokenRepository.findById(1L)
                .orElseThrow();
        refreshToken.updateNewToken("already update token");

        // when & then
        assertThatThrownBy(() -> tokenService.reissueTokens(oldTokens.getAccessToken(), oldTokens.getRefreshToken()))
                .isInstanceOf(TokenException.class)
                .hasMessage(ResponseCode.E20000.getMessage());
    }

    @DisplayName("DB에 저장돼 있는 refresh token을 삭제한다.")
    @Test
    void deleteRefreshTokne() {
        // given
        TokenCreationInfo info = TokenCreationInfo.builder()
            .memberId(1L)
            .role(Role.ROLE_USER)
            .build();
        Tokens tokens = tokenService.createTokens(info);

        // when
        tokenService.blockTokens(tokens.getAccessToken());

        // then
        assertThat(refreshTokenRepository.findById(1L).isEmpty()).isTrue();
    }

    @DisplayName("access token의 남은 유효시간을 계산해 해당 시간만큼 Redis에 저장한다.")
    @Test
    void saveAccessTokenToRedis() {
        // given
        TokenCreationInfo info = TokenCreationInfo.builder()
            .memberId(1L)
            .role(Role.ROLE_USER)
            .build();
        Tokens tokens = tokenService.createTokens(info);
        long expirationSec = jwtManager.calculateExpirationSec(tokens.getAccessToken(), Instant.now());
        tokenService.blockTokens(tokens.getAccessToken());

        // when
        BlackToken blackToken = blackTokenRepository.findById(tokens.getAccessToken())
            .orElseThrow();

        // then
        assertThat(blackToken.getToken()).isEqualTo(tokens.getAccessToken());
        assertThat(blackToken.getExpirationSec()).isEqualTo(expirationSec);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}