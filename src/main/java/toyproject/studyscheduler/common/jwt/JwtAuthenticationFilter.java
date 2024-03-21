package toyproject.studyscheduler.common.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTH_TYPE = "Bearer ";
    public static final String EXCEPTION_KEY = "exception";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> tokenWithBearer = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));
        tokenWithBearer.ifPresent(token -> authenticate(request, token));

        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request, String tokenWithBearer) {
        try {
            String accessToken = extractToken(tokenWithBearer);

            Authentication authentication = getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | IllegalArgumentException | IllegalStateException e) {
            request.setAttribute(EXCEPTION_KEY, e);
            log.error("e :: ", e);
        }
    }

    private String extractToken(String tokenWithBearer) {
        validateNone(tokenWithBearer);
        String authType = tokenWithBearer.substring(0, AUTH_TYPE.length());

        validateAuthType(authType);
        return tokenWithBearer.substring(AUTH_TYPE.length());
    }

    private void validateNone(String tokenWithBearer) {
        if (!StringUtils.hasText(tokenWithBearer)) {
            throw new IllegalArgumentException("토큰 값이 존재하지 않습니다.");
        }
    }

    private void validateAuthType(String authType) {
        if (!authType.equalsIgnoreCase(AUTH_TYPE)) {
            throw new IllegalArgumentException("AUTH_TYPE이 일치하지 않습니다. AUTH_TYPE :: " + authType);
        }
    }

    private Authentication getAuthentication(String accessToken) {
        String[] idAndRole = jwtProvider.parseAccessToken(accessToken);

        return new UsernamePasswordAuthenticationToken(
            idAndRole[0],
            "",
            Collections.singletonList(new SimpleGrantedAuthority(idAndRole[1]))
        );
    }
}