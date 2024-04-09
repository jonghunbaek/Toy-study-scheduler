package toyproject.studyscheduler.auth.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import toyproject.studyscheduler.auth.application.AuthService;
import toyproject.studyscheduler.token.application.TokenService;
import toyproject.studyscheduler.token.application.dto.TokenCreationInfo;
import toyproject.studyscheduler.auth.application.dto.SignInInfo;
import toyproject.studyscheduler.auth.web.dto.Tokens;

import static org.springframework.http.HttpHeaders.*;
import static toyproject.studyscheduler.common.util.ResponseManager.*;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/auth/sign-in")
    public void signIn(@RequestBody SignInInfo signInInfo, HttpServletResponse response) {
        TokenCreationInfo tokenCreationInfo = authService.login(signInInfo);
        Tokens tokens = tokenService.createTokens(tokenCreationInfo);

        setUpTokensToCookie(tokens, response);
    }

    @PostMapping("/auth/logout")
    public void logout(@RequestHeader(name = AUTHORIZATION) String accessToken, HttpServletResponse response) {
        tokenService.blockTokens(accessToken);
        clearTokensFromCookie(response);
    }
}
