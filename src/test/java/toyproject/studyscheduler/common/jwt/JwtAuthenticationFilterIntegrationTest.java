package toyproject.studyscheduler.common.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import toyproject.studyscheduler.auth.application.AuthService;
import toyproject.studyscheduler.auth.application.dto.SignUpInfo;

import static org.mockito.ArgumentMatchers.*;

@ActiveProfiles("test")
//@SpringBootTest
class JwtAuthenticationFilterIntegrationTest {

    private WebTestClient client;

    @MockBean
    AuthService authService;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    }

    @DisplayName("")
    @Test
    void test() {
        // given
        SignUpInfo info = SignUpInfo.builder()
            .email("abc@gmail.com")
            .password("12345")
            .name("abc")
            .build();

        // when & then
        client.post()
            .uri("/auth/sign-up")
            .bodyValue(info)
            .exchange()
            .expectStatus().isOk();
    }
}