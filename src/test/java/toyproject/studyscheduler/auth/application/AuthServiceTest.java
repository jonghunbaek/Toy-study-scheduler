package toyproject.studyscheduler.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.auth.application.dto.SignInInfo;
import toyproject.studyscheduler.member.application.dto.SignUpInfo;
import toyproject.studyscheduler.auth.exception.AuthException;
import toyproject.studyscheduler.member.application.MemberService;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    MemberService memberService;

    @DisplayName("로그인시 입력 받은 비밀번호와 저장된 비밀번호가 다를 경우 예외를 발생시킨다.")
    @Test
    void validatePassword() {
        // given
        SignUpInfo signUpInfo = SignUpInfo.builder()
                .name("hong")
                .password("1234")
                .email("hong@gmail.com")
                .build();
        memberService.saveNewMember(signUpInfo);

        SignInInfo incorrectInfo = new SignInInfo("hong@gmail.com", "12345");

        // when & then
        assertThatThrownBy(() -> authService.login(incorrectInfo))
                .isInstanceOf(AuthException.class);
    }
}