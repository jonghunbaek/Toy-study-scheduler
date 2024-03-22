package toyproject.studyscheduler.token.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.member.entity.Role;
import toyproject.studyscheduler.token.application.dto.TokenCreationInfo;
import toyproject.studyscheduler.token.entity.RefreshToken;
import toyproject.studyscheduler.token.repository.RefreshTokenRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class TokenServiceTest {

    @Autowired
    TokenService tokenService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

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
    void saveRefreshTokenWhenExist() throws InterruptedException {
        // given
        TokenCreationInfo info = TokenCreationInfo.builder()
            .memberId(1L)
            .role(Role.ROLE_USER)
            .build();

        // when
        Tokens oldTokens = tokenService.createTokens(info);

        // 일시정지 하지 않으면 시간 차이가 없어 결과적으로 같은 refresh token이 생성됨
        Thread.sleep(1000);
        Tokens newTokens = tokenService.createTokens(info);

        RefreshToken refreshToken = refreshTokenRepository.findById(1L)
            .orElseThrow();

        // then
        assertThat(oldTokens.getRefreshToken()).isNotEqualTo(refreshToken.getToken());
        assertThat(newTokens.getRefreshToken()).isEqualTo(refreshToken.getToken());
    }
}