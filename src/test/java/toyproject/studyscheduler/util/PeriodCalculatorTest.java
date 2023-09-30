package toyproject.studyscheduler.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class PeriodCalculatorTest {

    @DisplayName("강의 학습의 총 예상 기간을 계산한다.")
    @Test
    void calculateTotalExpectedPeriodWithLecture() {
        // given
        int planTimeInWeekDay = 60;
        int planTimeInWeekend = 120;
        int totalRunTime = 600;
        LocalDate startDate = LocalDate.of(2023, 9, 15);

        PeriodCalculator calculator = PeriodCalculator.builder()
                .planTimeInWeekday(planTimeInWeekDay)
                .planTimeInWeekend(planTimeInWeekend)
                .startDate(startDate)
                .build();

        // when
        int period = calculator.calculateTotalExpectedPeriod(totalRunTime);

        // then
        Assertions.assertThat(period).isEqualTo(8);
    }

    @DisplayName("DayOfWeek 학습 테스트")
    @Test
    void dayOfWeekStudyTest() {
        // given
        LocalDate localDate = LocalDate.of(2023, 9,29);

        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        // when
        DayOfWeek plus1 = dayOfWeek.plus(1);
        DayOfWeek plus2 = plus1.plus(1);
        DayOfWeek plus3 = plus2.plus(1);
        DayOfWeek plus4 = plus3.plus(1);

        // then
        assertThat(plus1.getValue()).isEqualTo(6);
        assertThat(plus2.getValue()).isEqualTo(7);
        assertThat(plus3.getValue()).isEqualTo(1);
        assertThat(plus4.getValue()).isEqualTo(2);
    }
}