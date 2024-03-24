package toyproject.studyscheduler.common.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import toyproject.studyscheduler.auth.web.dto.Tokens;

public class CookieManager {

    public static final String[] TOKEN_TYPE = {"access_token", "refresh_token"};
    public static final int COOKIE_MAX_AGE = 60 * 60;

    public static void setUpTokensToCookie(Tokens tokens, HttpServletResponse response) {
        ResponseCookie accessTokenCookie = createCookie(TOKEN_TYPE[0], tokens.getAccessToken(), false, COOKIE_MAX_AGE);
        ResponseCookie refreshTokenCookie = createCookie(TOKEN_TYPE[1], tokens.getRefreshToken(), true, COOKIE_MAX_AGE);

        setUpCookie(response, accessTokenCookie, refreshTokenCookie);
    }

    public static void clearTokensFromCookie(HttpServletResponse response) {
        ResponseCookie accessCookie = createCookie(TOKEN_TYPE[0], "", false, 0);
        ResponseCookie refreshCookie = createCookie(TOKEN_TYPE[1], "", false, 0);

        setUpCookie(response, accessCookie, refreshCookie);
    }

    private static ResponseCookie createCookie(String tokenType, String token, boolean isHttpOnly, long maxAge) {
        return ResponseCookie.from(tokenType, token)
            .httpOnly(isHttpOnly)
            .secure(true)
            .path("/")
            .maxAge(maxAge)
            .sameSite("None")
            .build();
    }

    private static void setUpCookie(HttpServletResponse response, ResponseCookie accessCookie, ResponseCookie refreshCookie) {
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
