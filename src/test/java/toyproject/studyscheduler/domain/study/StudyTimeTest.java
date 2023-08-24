package toyproject.studyscheduler.domain.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.lecture.Lecture;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;


class StudyTimeTest {

    @DisplayName("StudyTime 생성 시, 총 학습량 = 총 학습량 + 오늘 학습량 계산을 한다.")
    @Test
    void calculateTotalCompleteTime() {
        // given
        LocalDate yesterday = LocalDate.of(2023, 8, 7);
        Member member = createMember();
        Study lecture = createLecture(member);
        int totalCompleteTime = 200;
        int completeTimeYesterDay = 80;
        StudyTime studyTimeYesterday = createStudyTime(lecture, yesterday, completeTimeYesterDay, totalCompleteTime);

        // when
        LocalDate today = LocalDate.of(2023, 8, 10);
        int completeTimeToday = 100;
        StudyTime studyTimeToday = createStudyTime(lecture, today, completeTimeToday, totalCompleteTime + completeTimeToday);

        // then
        assertThat(studyTimeToday.getTotalCompleteTime()).isEqualTo(studyTimeYesterday.getTotalCompleteTime() + completeTimeToday);

    }

    @DisplayName("총 예상 시간과 총 학습 시간을 나눠 현재 학습율을 구한다.")
    @Test
    void calculateLearningRate() {
        // given
        LocalDate yesterday = LocalDate.of(2023, 8, 7);
        Member member = createMember();
        Study lecture = createLecture(member);
        int totalCompleteTime = 80;
        int completeTimeToday = 80;
        StudyTime studyTime = createStudyTime(lecture, yesterday, completeTimeToday, totalCompleteTime);

        // when
        double learningRate = studyTime.calculateLearningRate();

        // then
        assertThat(learningRate).isEqualTo(13.33);
    }

    private StudyTime createStudyTime(Study lecture, LocalDate today, int completeTimeToday, int totalCompleteTime) {
        return StudyTime.builder()
                .totalCompleteTime(totalCompleteTime)
                .today(today)
                .completeTimeToday(completeTimeToday)
                .study(lecture)
                .build();
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