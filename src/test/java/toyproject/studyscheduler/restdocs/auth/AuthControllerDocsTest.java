package toyproject.studyscheduler.restdocs.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import toyproject.studyscheduler.auth.application.AuthService;
import toyproject.studyscheduler.auth.application.dto.SignInInfo;
import toyproject.studyscheduler.auth.web.AuthController;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.restdocs.RestDocsSupport;
import toyproject.studyscheduler.token.application.TokenService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerDocsTest extends RestDocsSupport {

    private final AuthService authService = mock(AuthService.class);
    private final TokenService tokenService = mock(TokenService.class);


    @Override
    protected Object initController() {
        return new AuthController(authService, tokenService);
    }

    @DisplayName("로그인 API에 대한 REST Docs")
    @Test
    void signIn() throws Exception {
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
                .andExpect(cookie().value("refresh_token", refreshToken))
                // 첫 번째 인자는 해당 테스트의 식별할 수 있는 이름, 두 번째부턴 해당 테스트에 필요한 요소들
                .andDo(document("sign-in docs",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("사용자 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("사용자 비밀번호")
                        ),
                        responseCookies(
                                cookieWithName("access_token")
                                        .description("액세스 토큰"),
                                cookieWithName("refresh_token")
                                        .description("리프레쉬 토큰")
                        )
                ));
    }
}
