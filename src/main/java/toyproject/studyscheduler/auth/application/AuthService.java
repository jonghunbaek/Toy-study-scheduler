package toyproject.studyscheduler.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.auth.application.dto.MemberInfo;
import toyproject.studyscheduler.auth.application.dto.SignInInfo;
import toyproject.studyscheduler.auth.application.dto.SignUpInfo;
import toyproject.studyscheduler.auth.exception.AuthException;
import toyproject.studyscheduler.common.exception.ResponseCode;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.member.repository.MemberRepository;

import static toyproject.studyscheduler.common.exception.ResponseCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpInfo signUpInfo) {
        validateEmail(signUpInfo.getEmail());

        Member member = signUpInfo.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일이 있습니다.");
        }
    }

    public MemberInfo signIn(SignInInfo signInInfo) {
        Member member = findMember(signInInfo.getEmail());

        validatePassword(signInInfo.getPassword(), member.getPassword());

        return MemberInfo.of(member);
    }

    private Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 회원이 없습니다."));
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