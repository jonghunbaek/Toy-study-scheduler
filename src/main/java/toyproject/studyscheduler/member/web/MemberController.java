package toyproject.studyscheduler.member.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toyproject.studyscheduler.member.application.dto.SignUpInfo;
import toyproject.studyscheduler.member.application.MemberService;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/sign-up")
    public void signUp(@RequestBody SignUpInfo signUpInfo) {
        memberService.saveNewMember(signUpInfo);
    }
}
