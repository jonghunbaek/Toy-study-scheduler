package toyproject.studyscheduler.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.member.application.MemberService;
import toyproject.studyscheduler.token.application.dto.TokenCreationInfo;
import toyproject.studyscheduler.auth.application.dto.SignInInfo;
import toyproject.studyscheduler.auth.exception.AuthException;
import toyproject.studyscheduler.member.domain.entity.Member;

import static toyproject.studyscheduler.common.response.ResponseCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public TokenCreationInfo login(SignInInfo signInInfo) {
        Member member = memberService.findMemberBy(signInInfo.getEmail());

        validatePassword(signInInfo.getPassword(), member.getPassword());

        return TokenCreationInfo.of(member);
    }

    private void validatePassword(String requestPassword, String storedPassword) {
        if (isNotMatch(requestPassword, storedPassword)) {
            throw new AuthException(E10000);
        }
    }

    private boolean isNotMatch(String requestPassword, String storedPassword) {
        return !passwordEncoder.matches(requestPassword, storedPassword);
    }
}
