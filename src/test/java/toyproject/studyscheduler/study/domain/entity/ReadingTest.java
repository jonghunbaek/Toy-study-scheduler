package toyproject.studyscheduler.study.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ReadingTest {

    @DisplayName("종료되지 않은 학습의 경우 남은 학습 기간을 계산한다.")
    @Test
    void calculateExpectedEndDate() {
        // given
        StudyInformation information = createInformation("클린 코드", "클린 코드 작성", false);
        StudyPeriod period = StudyPeriod.fromStarting(LocalDate.of(2024, 4, 1));
        StudyPlan plan = new StudyPlan(30, 60);
        Reading reading = createReading(information, period, plan, null);

        // when
        LocalDate expectedDate = reading.calculateExpectedDate();

        // then
        assertThat(expectedDate).isEqualTo(LocalDate.of(2024,4,8));
    }

    private static Reading createReading(StudyInformation information, StudyPeriod period, StudyPlan plan, Member member) {
        return Reading.builder()
            .studyInformation(information)
            .studyPeriod(period)
            .studyPlan(plan)
            .authorName("로버트 마틴")
            .readPagePerMin(2)
            .totalPage(600)
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