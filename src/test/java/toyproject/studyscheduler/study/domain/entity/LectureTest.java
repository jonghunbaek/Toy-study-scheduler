package toyproject.studyscheduler.study.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LectureTest {

    @DisplayName("종료되지 않은 강의 학습이면 예상 학습 종료일을 계산한다.")
    @Test
    void calculateExpectedEndDate() {
        // given
        StudyInformation information = createInformation("김영한의 Spring", "Spring강의", false);
        StudyPeriod period = StudyPeriod.fromStarting(LocalDate.of(2024, 4, 3));
        StudyPlan plan = StudyPlan.fromStarting(30, 60);
        Lecture lecture = createLecture(information, period, plan, null);

        // when
        LocalDate expectedDate = lecture.calculateExpectedDate(0, period.getStartDate());

        // then
        assertThat(expectedDate).isEqualTo(LocalDate.of(2024,4,14));
    }

    @DisplayName("실제로 수행한 총 학습 시간을 인자로 받아 남은 학습량을 계산해서 반환한다. 남은 학습량이 0미만이면 0을 반환한다.")
    @ParameterizedTest
    @CsvSource({"200, 247", "500,0"})
    void calculateRemainingQuantity(int totalStudyMinutes, int result) {
        // given
        StudyInformation information = createInformation("김영한의 Spring", "Spring강의", false);
        StudyPeriod period = StudyPeriod.fromStarting(LocalDate.of(2024, 4, 3));
        StudyPlan plan = StudyPlan.fromStarting(30, 60);
        Lecture lecture = createLecture(information, period, plan, null);

        // when
        int remainingQuantity = lecture.calculateRemainingQuantity(totalStudyMinutes);

        // then
        assertThat(remainingQuantity).isEqualTo(result);
    }

    private Lecture createLecture(StudyInformation information, StudyPeriod period, StudyPlan plan, Member member) {
        return Lecture.builder()
            .studyInformation(information)
            .studyPeriod(period)
            .studyPlan(plan)
            .teacherName("김영한")
            .totalRuntime(447)
            .member(member)
            .build();
    }

    private StudyInformation createInformation(String title, String description, boolean isTermination) {
        return StudyInformation.builder()
            .title(title)
            .description(description)
            .isTermination(isTermination)
            .build();
    }
}