package toyproject.studyscheduler.auth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import toyproject.studyscheduler.auth.application.AuthService;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
}
