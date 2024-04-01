package toyproject.studyscheduler.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.studyscheduler.auth.application.dto.SignUpInfo;
import toyproject.studyscheduler.common.response.ResponseCode;
import toyproject.studyscheduler.common.response.ResponseForm;
import toyproject.studyscheduler.member.domain.Role;
import toyproject.studyscheduler.token.application.TokenService;

import java.time.Instant;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class JwtAuthenticationFilterIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtManager jwtManager;
    @MockBean
    TokenService tokenService;

    @DisplayName("인가가 필요 없는 요청 테스트")
    @Test
    void notNecessaryAuthTest() throws Exception {
        // given
        SignUpInfo info = SignUpInfo.builder()
            .email("abc@gmail.com")
            .password("12345")
            .name("abc")
            .build();

        // when & then
        mockMvc.perform(post("/auth/sign-up")
                .content(objectMapper.writeValueAsString(info))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isOk());
    }

    @DisplayName("인가가 필요한 요청 테스트 - 토큰이 정상인 경우")
    @Test
    void necessaryAuthTest() throws Exception {
        // given
        String accessToken = "Bearer " + jwtManager.createAccessToken(1L, Role.ROLE_USER, Instant.now());

        // when & then
        mockMvc.perform(post("/auth/logout")
            .header(AUTHORIZATION, accessToken)
            .cookie(new Cookie("access_token", accessToken), new Cookie("refresh_token", "123456"))
            .with(csrf())
        )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("인가가 필요한 요청 테스트 - 토큰 타입이 다른 경우")
    @Test
    void necessaryAuthTestWhenAuthTypeDifferent() throws Exception {
        // given
        String accessToken = "Liontiger " + jwtManager.createAccessToken(1L, Role.ROLE_USER, Instant.now());
        ResponseForm<Object> response = ResponseForm.of(ResponseCode.E00001);
        String content = objectMapper.writeValueAsString(response);

        // when & then
        mockMvc.perform(post("/auth/logout")
                .header(AUTHORIZATION, accessToken)
                .cookie(new Cookie("access_token", accessToken), new Cookie("refresh_token", "123456"))
                .with(csrf())
            )
            .andDo(print())
            .andExpect(status().is(401))
            .andExpect(content().string(content))
            .andReturn();
    }

    @DisplayName("인가가 필요한 요청 테스트 - 토큰이 만료된 경우")
    @Test
    void necessaryAuthTestWhenTokenExpired() throws Exception {
        // given
        String accessToken = "Bearer " + jwtManager.createAccessToken(1L, Role.ROLE_USER, Instant.now());
        ResponseForm<Object> response = ResponseForm.of(ResponseCode.E00003);
        String content = objectMapper.writeValueAsString(response);

        sleep(5000);

        // when & then
        mockMvc.perform(post("/auth/logout")
                .header(AUTHORIZATION, accessToken)
                .cookie(new Cookie("access_token", accessToken), new Cookie("refresh_token", "123456"))
                .with(csrf())
            )
            .andDo(print())
            .andExpect(content().string(content))
            .andExpect(status().is(401));
    }

    @DisplayName("인가가 필요한 요청 테스트 - 토큰이 위조된 경우")
    @Test
    void necessaryAuthTestWhenTokenForgery() throws Exception {
        // given
        String accessToken = "Bearer " + jwtManager.createAccessToken(1L, Role.ROLE_USER, Instant.now()) + "forgery";
        ResponseForm<Object> response = ResponseForm.of(ResponseCode.E00004);
        String content = objectMapper.writeValueAsString(response);

        // when & then
        mockMvc.perform(post("/auth/logout")
                .header(AUTHORIZATION, accessToken)
                .cookie(new Cookie("access_token", accessToken), new Cookie("refresh_token", "123456"))
                .with(csrf())
            )
            .andDo(print())
            .andExpect(content().string(content))
            .andExpect(status().is(401));
    }

    @DisplayName("인가가 필요한 요청 테스트 - 토큰 형식이 잘못된 경우")
    @Test
    void necessaryAuthTestWhenTokenMalformed() throws Exception {
        // given
        String accessToken = "Bearer " + " " + jwtManager.createAccessToken(1L, Role.ROLE_USER, Instant.now());
        ResponseForm<Object> response = ResponseForm.of(ResponseCode.E00006);
        String content = objectMapper.writeValueAsString(response);

        // when & then
        mockMvc.perform(post("/auth/logout")
                .header(AUTHORIZATION, accessToken)
                .cookie(new Cookie("access_token", accessToken), new Cookie("refresh_token", "123456"))
                .with(csrf())
            )
            .andDo(print())
            .andExpect(content().string(content))
            .andExpect(status().is(401));
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}