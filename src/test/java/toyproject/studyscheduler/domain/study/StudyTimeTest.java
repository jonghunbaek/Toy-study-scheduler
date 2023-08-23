package toyproject.studyscheduler.domain.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.lecture.Lecture;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;


class StudyTimeTest {

    @DisplayName("총 예상 시간과 총 학습 시간을 계산해 현재 학습율을 구한다.")
    @Test
    void calculateLearningRate() {
        // given
        Member member = createMember();
        Study lecture = createLecture(member);
        StudyTime studyTime = StudyTime.builder()
            .totalCompleteTime(80)
            .today(LocalDate.of(2023, 8, 21))
            .completeTimeToday(30)
            .study(lecture)
            .build();

        // when
        double learningRate = studyTime.calculateLearningRate();

        // then
        assertThat(learningRate).isEqualTo(13.33);
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

    private static Lecture createLecture(Member member) {
        return Lecture.builder()
            .title("김영한의 스프링")
            .description("스프링 핵심 강의")
            .teacherName("김영한")
            .totalExpectedTime(600)
            .planTimeInWeekDay(30)
            .planTimeInWeekend(100)
            .startDate(LocalDate.of(2023,8,12))
            .endDate(LocalDate.of(2023,8,30))
            .member(member)
            .build();
    }
}