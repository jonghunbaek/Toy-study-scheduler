package toyproject.studyscheduler.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.member.application.dto.SignUpInfo;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.member.exception.MemberException;
import toyproject.studyscheduler.member.repository.MemberRepository;

import java.util.Optional;

import static toyproject.studyscheduler.common.response.ResponseCode.*;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveNewMember(SignUpInfo signUpInfo) {
        validateEmail(signUpInfo.getEmail());

        Member member = signUpInfo.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new MemberException(E10001);
        }
    }

    public Member findMemberBy(Long memberId) {
        return ifPresentOrElseThrow(memberRepository.findById(memberId), "id :: " + memberId);
    }

    public Member findMemberBy(String email) {
        return ifPresentOrElseThrow(memberRepository.findByEmail(email), "email :: " + email);
    }

    private Member ifPresentOrElseThrow(Optional<Member> memberOpt, String message) {
        return memberOpt.orElseThrow(() -> new MemberException(message, E10002));
    }
}
