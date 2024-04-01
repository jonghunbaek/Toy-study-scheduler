package toyproject.studyscheduler.studyplan.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.entity.Reading;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class StudyPlanTest {

    @DisplayName("학습 계획 생성시 예상 종료일을 계산하여 저장한다.")
    @Test
    void calculateExpectedEndDate() {
        // given
        StudyInformation information = createInformation("클린 코드", "클린 코드 작성", false);
        StudyPeriod period = StudyPeriod.fromStarting(LocalDate.now());
        Reading reading = createReading(information, period, null);

        // when
        StudyPlan plan = StudyPlan.from(30, 60, reading);

        // then
        assertThat(plan.getExpectedEndDate()).isEqualTo(LocalDate.of(2024,4,9));
    }

    private static Reading createReading(StudyInformation information, StudyPeriod period, Member member) {
        return Reading.builder()
            .studyInformation(information)
            .studyPeriod(period)
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