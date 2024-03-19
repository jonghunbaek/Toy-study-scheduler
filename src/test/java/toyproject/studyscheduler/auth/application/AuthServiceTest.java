package toyproject.studyscheduler.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.auth.application.dto.MemberInfo;
import toyproject.studyscheduler.auth.application.dto.SignInInfo;
import toyproject.studyscheduler.auth.application.dto.SignUpInfo;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("회원 정보를 전달 받아 회원 가입을 한다.")
    @Test
    void signUp() {
        // given
        SignUpInfo signUpInfo = SignUpInfo.builder()
                .email("abc@gmail.com")
                .password("12345")
                .name("abc")
                .build();

        // when
        authService.signUp(signUpInfo);
        Member member = memberRepository.findAll().get(0);

        // then
        assertThat(member.getEmail()).isEqualTo("abc@gmail.com");
    }

    @DisplayName("회원 가입 시 동일한 이메일이 DB에 존재하면 예외를 발생한다.")
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
        assertThatThrownBy(() -> authService.signUp(signUpInfo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일이 있습니다.");
    }

    @DisplayName("로그인시 입력 받은 비밀번호와 저장된 비밀번호를 비교한다.")
    @Test
    void validatePassword() {
        // given
        memberRepository.save(Member.builder()
                .email("abc@gmail.com")
                .password("1234")
                .build());
        SignInInfo correct = new SignInInfo("abc@gmail.com", "1234");
        SignInInfo incorrect = new SignInInfo("abc@gmail.com", "12345");

        // when
        MemberInfo memberInfo = authService.signIn(correct);

        // then
        assertThat(memberInfo.getMemberId()).isEqualTo(1);
    }
}