package toyproject.studyscheduler.study.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.exception.StudyException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static toyproject.studyscheduler.common.exception.GlobalException.*;

class StudyTest {

    @DisplayName("학습 예상 종료일을 계산할 때 학습이 이미 종료된 상태면 예외를 던진다.")
    @Test
    void validateTermination() {
        StudyInformation information = createInformation("클린 코드", "클린 코드 작성", true);
        StudyPeriod period = StudyPeriod.fromStarting(LocalDate.of(2024, 4, 1));
        StudyPlan plan = StudyPlan.fromStarting(30, 60);
        Reading reading = createReading(information, period, plan, null);

        // when & then
        assertThatThrownBy(() -> reading.calculateExpectedDate(0, null))
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
        boolean isTermination = reading.terminateIfSatisfiedStudyQuantity(300, LocalDate.of(2024, 4, 11));

        // then
        assertAll(
            () -> assertEquals(isTermination, reading.getStudyInformation().isTermination()),
            () -> assertEquals(LocalDate.of(2024,4,11), reading.getStudyPeriod().getEndDate())
        );
    }

    @DisplayName("일일 학습 수행일이 학습 시작일 보다 빠르면 예외를 발생한다.")
    @Test
    void validateStudyDateEarlierThanStartDate() {
        // given
        StudyInformation information = createInformation("클린 코드", "클린 코드 작성", false);
        StudyPeriod period = StudyPeriod.fromStarting(LocalDate.of(2024, 4, 1));
        StudyPlan plan = StudyPlan.fromStarting(30, 60);
        Reading reading = createReading(information, period, plan, null);

        // when & then
        assertThatThrownBy(() -> reading.validateStudyDateEarlierThanStartDate(LocalDate.of(2024,3,31)))
            .isInstanceOf(StudyException.class)
            .hasMessage("학습 수행일이 학습 시작일 보다 빠릅니다." + DETAIL_DELIMITER + "studyDate :: " + "2024-03-31");

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