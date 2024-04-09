package toyproject.studyscheduler.member.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.member.application.dto.SignUpInfo;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.member.exception.MemberException;
import toyproject.studyscheduler.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("회원 가입 시 동일한 이메일이 DB에 존재하면 예외가 발생한다.")
    @Test
    void signUpWhenExistsSameEmail() {
        // given
        memberRepository.save(Member.builder()
            .email("abc@gmail.com")
            .build());

        SignUpInfo signUpInfo = SignUpInfo.builder()
            .email("abc@gmail.com")
            .password("12345")
            .name("abc")
            .build();

        // when & then
        assertThatThrownBy(() -> memberService.saveNewMember(signUpInfo))
            .isInstanceOf(MemberException.class)
            .hasMessage("이미 존재하는 이메일이 있습니다.");
    }

    @DisplayName("아이디로 회원을 조회한다. 존재하지 않으면 예외가 발생한다.")
    @Test
    void findById() {
        Member findMember = memberService.findMemberBy("iphone@gmail.com");

        assertThat(findMember.getId()).isEqualTo(1L);
        assertThatThrownBy(() -> memberService.findMemberBy(2L))
            .isInstanceOf(MemberException.class)
            .hasMessage("존재하는 회원이 없습니다. detail => id :: 2");
    }

    @DisplayName("이메일로 회원을 조회한다. 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void findByEmail() {
        assertThatThrownBy(() -> memberService.findMemberBy("1234@gmail.com"))
            .isInstanceOf(MemberException.class)
            .hasMessage("존재하는 회원이 없습니다. detail => email :: 1234@gmail.com");
    }
}