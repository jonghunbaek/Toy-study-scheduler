package toyproject.studyscheduler.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import toyproject.studyscheduler.api.service.member.MemberService;

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
    public String signUp() {
        return "signUp";
    }
}
