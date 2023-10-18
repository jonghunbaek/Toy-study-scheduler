package toyproject.studyscheduler.domain.study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.lecture.Lecture;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StudyTest {

    @DisplayName("학습을 완료한다.")
    @Test
    void terminateStudy() {
        // given
        LocalDate startDate = LocalDate.of(2023, 10, 1);
        LocalDate expectedEndDate = LocalDate.of(2023, 10, 15);
        Member member = createMember();

        Lecture lecture = createLecture(startDate, expectedEndDate, member);

        // when, then
        assertThat(lecture.isTermination()).isFalse();
        assertThat(lecture).extracting("realEndDate", "expectedEndDate")
            .contains(LocalDate.EPOCH, LocalDate.of(2023, 10, 15));

        LocalDate realEndDate = LocalDate.of(2023, 10, 21);
        lecture.terminateStudyIn(realEndDate);

        assertThat(lecture.isTermination()).isTrue();
        assertThat(lecture).extracting("realEndDate", "expectedEndDate")
            .contains(LocalDate.of(2023, 10, 21), LocalDate.of(2023, 10, 15));
    }

    private static Member createMember() {
        return Member.builder()
            .email("hong@gmail.com")
            .password("zxcv1234")
            .name("hong")
            .accountType(AccountType.ACTIVE)
            .originProfileImage("1234")
            .storedProfileImage("4151")
            .build();
    }

    private static Lecture createLecture(LocalDate startDate, LocalDate expectedEndDate, Member member) {
        return Lecture.builder()
            .title("김영한의 스프링")
            .description("스프링 핵심 강의")
            .teacherName("김영한")
            .planTimeInWeekday(30)
            .planTimeInWeekend(100)
            .startDate(startDate)
            .member(member)
            .totalRuntime(600)
            .build();
    }
}