package toyproject.studyscheduler.api.service.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.api.controller.request.member.SaveMemberRequestDto;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;

import static org.junit.jupiter.api.Assertions.*;
import static toyproject.studyscheduler.domain.member.AccountType.*;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @DisplayName("회원의 정보를 전달받아 저장한다.")
    @Test
    void saveMember() {
        // given
        SaveMemberRequestDto requestDto = SaveMemberRequestDto.builder()
            .email("hong@gmail.com")
            .password("zxcv1234")
            .name("홍길동")
            .accountType(ACTIVE)
            .build();

        // when

        // then

    }

    @DisplayName("회원의 이메일과 비밀번호가 일치하는지 확인한다.")
    @Test
    void checkEmailAndPw() {
        // given


        // when

        // then

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