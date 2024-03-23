package toyproject.studyscheduler.token.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import toyproject.studyscheduler.token.application.TokenService;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private TokenService tokenService;


}
