package toyproject.studyscheduler.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import toyproject.studyscheduler.common.response.ResponseForm;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;
import static toyproject.studyscheduler.common.jwt.JwtAuthenticationFilter.*;
import static toyproject.studyscheduler.common.response.ResponseCode.*;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseForm exceptionResponse = createExceptionMessage((Exception) request.getAttribute(EXCEPTION_KEY));
        setUpResponse(response, exceptionResponse);
    }

    private ResponseForm createExceptionMessage(Exception e) {
        if (e instanceof ExpiredJwtException) {
            return ResponseForm.of(E00003);
        }

        if (e instanceof SignatureException) {
            return ResponseForm.of(E00004);
        }

        if (e instanceof IllegalStateException) {
            return ResponseForm.of(E00005);
        }

        return ResponseForm.of(E00001);
    }

    private void setUpResponse(HttpServletResponse response, ResponseForm responseForm) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(UNAUTHORIZED.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseForm));
    }
}
