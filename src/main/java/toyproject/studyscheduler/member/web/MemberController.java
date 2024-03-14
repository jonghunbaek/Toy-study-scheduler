package toyproject.studyscheduler.member.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.member.web.dto.SignUp;
import toyproject.studyscheduler.member.web.dto.SignInRequest;
import toyproject.studyscheduler.member.application.MemberService;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign-up")
    public void signUp(SignUp signUp) {

    }

    @PostMapping("/sign-in")
    public void signIn(@ModelAttribute @Valid SignInRequest signInRequest) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(signInRequest.getEmail(), signInRequest.getPassword());
        Authentication authenticateResponse = authenticationManager.authenticate(authenticationRequest);

        SecurityContextHolder.getContext().setAuthentication(authenticateResponse);
    }
}
