package toyproject.studyscheduler.member.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.member.entity.Member;

import static org.assertj.core.api.Assertions.*;
import static toyproject.studyscheduler.member.entity.domain.AccountType.*;

class MemberTest {

    @DisplayName("비밀번호를 인자로 받아 자신의 비밀번호와 일치하는 지 확인한다.")
    @Test
    void isMatching() {
        // given
        Member member = Member.builder()
            .email("hong@gmail.com")
            .password("zxcv1234")
            .accountType(ACTIVE)
            .name("홍길동")
            .build();

        // when
        String password = "zxcv1234";

        // then
        assertThat(member.isMatching(password)).isTrue();
    }
}