package toyproject.studyscheduler.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.controller.request.member.SaveMemberRequestDto;
import toyproject.studyscheduler.controller.request.member.SignInRequestDto;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.service.member.MemberService;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
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
    public String signIn(@ModelAttribute @Valid SignInRequestDto signInRequestDto, HttpServletRequest request) {
        Member member = memberService.signIn(signInRequestDto);
        request.setAttribute("login", member.getId());

        return "로그인 이후 페이지";
    }
}
