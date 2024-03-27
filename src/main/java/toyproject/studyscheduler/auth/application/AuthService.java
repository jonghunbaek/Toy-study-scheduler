package toyproject.studyscheduler.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.token.application.dto.TokenCreationInfo;
import toyproject.studyscheduler.auth.application.dto.SignInInfo;
import toyproject.studyscheduler.auth.application.dto.SignUpInfo;
import toyproject.studyscheduler.auth.exception.AuthException;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.member.repository.MemberRepository;

import static toyproject.studyscheduler.common.response.ResponseCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveNewMember(SignUpInfo signUpInfo) {
        validateEmail(signUpInfo.getEmail());

        Member member = signUpInfo.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new AuthException(E10001);
        }
    }

    public TokenCreationInfo login(SignInInfo signInInfo) {
        Member member = findMember(signInInfo.getEmail());

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

    private Member findMember(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("존재하는 회원이 없습니다."));
    }
}
