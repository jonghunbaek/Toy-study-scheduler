package toyproject.studyscheduler.study.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        StudyPlan plan = new StudyPlan(30, 60);
        Lecture lecture = createLecture(information, period, plan, null);

        // when
        LocalDate expectedDate = lecture.calculateExpectedDate();

        // then
        assertThat(expectedDate).isEqualTo(LocalDate.of(2024,4,14));
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