package toyproject.studyscheduler.common.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import toyproject.studyscheduler.common.response.ResponseForm;

import static org.springframework.http.HttpStatus.*;
import static toyproject.studyscheduler.common.response.ResponseCode.*;
import static toyproject.studyscheduler.common.util.ResponseManager.*;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        setUpResponse(response, ResponseForm.of(E00002), FORBIDDEN);
    }
}
