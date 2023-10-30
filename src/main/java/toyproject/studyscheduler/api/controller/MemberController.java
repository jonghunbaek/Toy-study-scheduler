package toyproject.studyscheduler.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/members")
@Controller
public class MemberController {

    @GetMapping("/sign-in")
    public String signIn() {
        return "signIn";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "signUp";
    }
}
