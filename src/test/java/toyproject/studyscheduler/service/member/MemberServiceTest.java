package toyproject.studyscheduler.service.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.controller.request.member.SignUp;
import toyproject.studyscheduler.controller.request.member.SignIn;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;
import static toyproject.studyscheduler.domain.member.AccountType.*;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void clean() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("회원의 정보를 전달받아 저장한다.")
    @Test
    void saveMember() {
        // given
        SignUp requestDto = SignUp.builder()
            .email("hong@gmail.com")
            .password("zxcv1234")
            .name("홍길동")
            .build();

        // when
        memberService.saveMember(requestDto);
        Member member = memberRepository.findAll().get(0);

        // then
        assertThat(member).extracting("name", "email")
            .contains("홍길동", "hong@gmail.com");
    }

    @DisplayName("회원을 생성할 때 존재하는 이메일이면 예외를 발생시킨다.")
    @Test
    void saveMemberWhenEmailExist() {
        // given
        memberRepository.save(createMember());

        SignUp requestDto = SignUp.builder()
            .email("hong@gmail.com")
            .password("zxcv1234")
            .name("홍길동")
            .build();

        // when & then
        assertThatThrownBy(() -> memberService.saveMember(requestDto))
            .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 이메일이 이미 존재합니다.");
    }

    @DisplayName("이메일로 회원을 조회하여 비밀번호가 일치하는 지 확인한다.")
    @Test
    void signIn() {
        // given
        memberRepository.save(createMember());
        String email = "hong@gmail.com";
        String password = "zxcv1234";

        SignIn dto = SignIn.of(email, password);
        // when
        Member member = memberService.signIn(dto);

        // then
        assertThat(member.getEmail()).isEqualTo("hong@gmail.com");
    }

    private Member createMember() {
        return Member.builder()
            .email("hong@gmail.com")
            .password("zxcv1234")
            .name("hong")
            .accountType(ACTIVE)
            .build();
    }
}