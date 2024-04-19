package toyproject.studyscheduler.study.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.exception.StudyException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StudyTest {

    @DisplayName("학습 예상 종료일을 계산할 때 학습이 이미 종료된 상태면 예외를 던진다.")
    @Test
    void validateTermination() {
        StudyInformation information = createInformation("클린 코드", "클린 코드 작성", true);
        StudyPeriod period = StudyPeriod.fromStarting(LocalDate.of(2024, 4, 1));
        StudyPlan plan = StudyPlan.fromStarting(30, 60);
        Reading reading = createReading(information, period, plan, null);

        // when & then
        assertThatThrownBy(() -> reading.calculateExpectedDate())
            .isInstanceOf(StudyException.class)
            .hasMessage("해당 학습은 이미 종료되었습니다.");
    }

    @DisplayName("실제 수행한 총 학습 시간이 예정된 학습량 이상이면 학습을 종료한다.")
    @Test
    void terminateIfSatisfiedStudyQuantity() {
        // given
        StudyInformation information = createInformation("클린 코드", "클린 코드 작성", false);
        StudyPeriod period = StudyPeriod.fromStarting(LocalDate.of(2024, 4, 1));
        StudyPlan plan = StudyPlan.fromStarting(30, 60);
        Reading reading = createReading(information, period, plan, null);

        // when
        boolean isTermination = reading.terminateIfSatisfiedStudyQuantity(300);

        // then
        assertThat(isTermination).isEqualTo(reading.getStudyInformation().isTermination());
    }

    private Reading createReading(StudyInformation information, StudyPeriod period, StudyPlan plan, Member member) {
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