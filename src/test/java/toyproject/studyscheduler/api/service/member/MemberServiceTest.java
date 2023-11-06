package toyproject.studyscheduler.api.service.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import toyproject.studyscheduler.api.controller.request.member.SaveMemberRequestDto;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
        SaveMemberRequestDto requestDto = SaveMemberRequestDto.builder()
            .email("hong@gmail.com")
            .password("zxcv1234")
            .name("홍길동")
            .accountType(ACTIVE)
            .build();

        // when
        memberService.saveMember(requestDto);
        Member member = memberRepository.findAll().get(0);

        // then
        assertThat(member).extracting("name", "email")
            .contains("홍길동", "hong@gmail.com");
    }

    @DisplayName("회원을 저장할 때 존재하는 이메일이면 예외를 발생시킨다.")
    @Test
    void saveMemberWhenEmailExist() {
        // given
        memberRepository.save(createMember());

        SaveMemberRequestDto requestDto = SaveMemberRequestDto.builder()
            .email("hong@gmail.com")
            .password("zxcv1234")
            .name("홍길동")
            .accountType(ACTIVE)
            .build();

        // when & then
        assertThatThrownBy(() -> memberService.saveMember(requestDto))
            .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 이메일이 이미 존재합니다.");
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