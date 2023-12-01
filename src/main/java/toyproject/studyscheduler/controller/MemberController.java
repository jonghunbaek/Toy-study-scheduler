package toyproject.studyscheduler.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.controller.request.member.SignUp;
import toyproject.studyscheduler.controller.request.member.SignIn;
import toyproject.studyscheduler.service.member.MemberService;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public void signUp(SignUp signUp) {

    }

    @PostMapping("/sign-in")
    public void signIn(@ModelAttribute @Valid SignIn signIn) {

    }
}
