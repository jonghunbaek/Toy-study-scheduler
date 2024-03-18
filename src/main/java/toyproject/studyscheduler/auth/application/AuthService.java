package toyproject.studyscheduler.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import toyproject.studyscheduler.auth.application.dto.MemberInfo;
import toyproject.studyscheduler.auth.application.dto.SignInInfo;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.member.repository.MemberRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberInfo signIn(SignInInfo signInInfo) {
        Member member = memberRepository.findByEmail(signInInfo.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("존재하는 회원이 없습니다."));

        validatePassword(signInInfo.getPassword(), member.getPassword());

        return MemberInfo.of(member);
    }

    private void validatePassword(String requestPassword, String storedPassword) {
        if (isNotMatch(requestPassword, storedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private boolean isNotMatch(String requestPassword, String storedPassword) {
        return !passwordEncoder.matches(requestPassword, storedPassword);
    }
}
