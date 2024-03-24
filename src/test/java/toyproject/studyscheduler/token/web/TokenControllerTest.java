package toyproject.studyscheduler.token.web;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.common.jwt.JwtAuthenticationFilter;
import toyproject.studyscheduler.token.application.TokenService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = TokenController.class,
    excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)}
)
class TokenControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    TokenService tokenService;

    @WithMockUser
    @DisplayName("헤더, 쿠키에서 access, refresh token을 추출해 token을 재발행한다.")
    @Test
    void reissueTokens() throws Exception {
        // given
        String oldAccess ="1234";
        String oldRefresh = "123456";
        Tokens newTokens = Tokens.of("abc", "abcde");

        when(tokenService.reissueTokens(any(), any()))
            .thenReturn(newTokens);

        // when & then
        mockMvc.perform(
            post("/token/reissue")
                .cookie(new Cookie("refresh_token", oldRefresh))
                .header(AUTHORIZATION, oldAccess)
                .with(csrf())
        )
            .andExpect(status().isOk())
            .andExpect(cookie().value("access_token", newTokens.getAccessToken()))
            .andExpect(cookie().value("refresh_token", newTokens.getRefreshToken()));
    }
}