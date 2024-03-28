package toyproject.studyscheduler.common.jwt;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import toyproject.studyscheduler.token.entity.BlackToken;
import toyproject.studyscheduler.token.repository.redis.BlackTokenRepository;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

class JwtAuthenticationFilterUnitTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtManager jwtManager;
    private BlackTokenRepository blackTokenRepository;

    @BeforeEach
    void setUp() {
        jwtManager = mock(JwtManager.class);
        blackTokenRepository = mock(BlackTokenRepository.class);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtManager, blackTokenRepository);
    }

    @DisplayName("헤더에 토큰이 존재하지 않으면 예외를 발생한다.")
    @Test
    void validateNone() throws IOException, ServletException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        request.setRequestURI("/studies");
        request.addHeader(AUTHORIZATION, "");

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        Exception exception = (Exception) request.getAttribute("exception");

        // then
        assertThat(exception.getMessage()).isEqualTo("토큰 값이 존재하지 않습니다.");
    }

    @DisplayName("토큰의 타입이 일치하지 않으면 예외를 발생한다.")
    @Test
    void validateTokenType() throws IOException, ServletException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        request.setRequestURI("/studies");
        request.addHeader(AUTHORIZATION, "anytype randomtoken");

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        Exception exception = (Exception) request.getAttribute("exception");

        // then
        assertThat(exception.getMessage()).isEqualTo("AUTH_TYPE이 일치하지 않습니다. AUTH_TYPE :: anytype");
    }

    @DisplayName("토큰이 블래리스트 처리되어 있다면 예외를 발생한다.")
    @Test
    void validateBlackToken() throws IOException, ServletException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        request.setRequestURI("/studies");
        request.addHeader(AUTHORIZATION, "Bearer randomtoken");

        when(blackTokenRepository.findById(any()))
            .thenReturn(Optional.of(new BlackToken()));

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        Exception exception = (Exception) request.getAttribute("exception");

        // then
        assertThat(exception.getMessage()).isEqualTo("해당 토큰은 로그아웃 처리 된 토큰 입니다.");
    }
}