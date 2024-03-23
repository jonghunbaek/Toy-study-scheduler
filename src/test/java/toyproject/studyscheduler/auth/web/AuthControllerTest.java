package toyproject.studyscheduler.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.filter.OncePerRequestFilter;
import toyproject.studyscheduler.auth.application.AuthService;
import toyproject.studyscheduler.auth.application.dto.SignInInfo;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.common.config.security.SecurityConfig;
import toyproject.studyscheduler.common.jwt.JwtAuthenticationFilter;
import toyproject.studyscheduler.common.jwt.JwtManager;
import toyproject.studyscheduler.token.application.TokenService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)}
)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    AuthService authService;
    @MockBean
    TokenService tokenService;

    @WithMockUser
    @DisplayName("로그인 시 access, refresh token을 쿠키에 담아 반환한다.")
    @Test
    void setUpCookiesWhenSignIn() throws Exception {
        // given
        SignInInfo signInInfo = new SignInInfo("abc@gmail.com", "password");
        String accessToken = "1234";
        String refreshToken = "123456";
        Tokens tokens = Tokens.of(accessToken, refreshToken);

        when(tokenService.createTokens(any()))
                .thenReturn(tokens);

        // when & then
        mockMvc.perform(post("/auth/sign-in")
                .content(objectMapper.writeValueAsString(signInInfo))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(cookie().value("access_token", accessToken))
                .andExpect(cookie().value("refresh_token", refreshToken));
    }
}