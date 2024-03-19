package toyproject.studyscheduler.auth.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.auth.application.dto.MemberInfo;
import toyproject.studyscheduler.auth.application.dto.SignInInfo;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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


        // when

        // then

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