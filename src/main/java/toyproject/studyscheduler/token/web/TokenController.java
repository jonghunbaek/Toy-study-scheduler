package toyproject.studyscheduler.token.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import toyproject.studyscheduler.auth.web.dto.Tokens;
import toyproject.studyscheduler.token.application.TokenService;

import static toyproject.studyscheduler.common.util.ResponseManager.*;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/token/reissue")
    public void reissueTokens(@CookieValue(name = "refresh_token") String refreshToken,
                              @RequestHeader(name = HttpHeaders.AUTHORIZATION) String accessToken,
                              HttpServletResponse response) {

        Tokens tokens = tokenService.reissueTokens(refreshToken, accessToken);

        setUpTokensToCookie(tokens, response);
    }
}
