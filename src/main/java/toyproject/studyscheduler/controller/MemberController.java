package toyproject.studyscheduler.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import toyproject.studyscheduler.controller.request.member.SaveMemberRequestDto;
import toyproject.studyscheduler.controller.request.member.SignInRequestDto;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.service.member.MemberService;

import java.net.http.HttpRequest;

@RequiredArgsConstructor
@RequestMapping("/members")
@Controller
public class MemberController {

    private final MemberService memberService;

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

    @GetMapping("/sign-in")
    public String signInForm() {
        return "signIn";
    }

    @PostMapping("/sign-in")
    public String signIn(SignInRequestDto signInRequestDto, BindingResult bindingResult, HttpServletRequest request) {

        Member member = null;
        try {
            member = memberService.signIn(signInRequestDto);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("loginFail", "로그인 실패");
        }

        if (bindingResult.hasErrors()) {
            return "로그인 페이지";
        }

        request.setAttribute("login", member.getId());

        return "로그인 이후 페이지";
    }
}
