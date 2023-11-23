package toyproject.studyscheduler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import toyproject.studyscheduler.controller.request.member.SaveMemberRequestDto;
import toyproject.studyscheduler.service.member.MemberService;

import java.net.http.HttpRequest;

@RequiredArgsConstructor
@RequestMapping("/members")
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/sign-in")
    public String signInForm() {
        return "signIn";
    }

    @PostMapping("/sign-in")
    public String signIn(String email, String password, HttpRequest request) {
        return null;
    }

    @GetMapping("/sign-up")
    public String signUpForm() {
        return "signUp";
    }

    @PostMapping("/sign-up")
    public void signUp(SaveMemberRequestDto saveMemberRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return;
        }
    }
}
