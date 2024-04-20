package toyproject.studyscheduler.study.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.study.domain.PeriodCalculator;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

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
        PeriodCalculator periodCalculator = createPeriodCalculator(planTimeInWeekDay, planTimeInWeekend, startDate);

        // when
        int period = periodCalculator.calculateExpectedPeriod(600);

        // then
        assertThat(period).isEqualTo(8);
    }

    @DisplayName("월요일에 시작한 경우 독서 학습의 총 예상 기간을 계산한다.")
    @Test
    void calculateTotalExpectedPeriodWithReadingMonday() {
        // given
        int planTimeInWeekDay = 30;
        int planTimeInWeekend = 60;
        int totalPage = 700;
        int readPagePerMin = 2;
        LocalDate startDate = LocalDate.of(2023, 9, 11);

        PeriodCalculator periodCalculator = createPeriodCalculator(planTimeInWeekDay, planTimeInWeekend, startDate);

        // when
        int period = periodCalculator.calculateExpectedPeriod(totalPage / readPagePerMin);

        // then
        assertThat(period).isEqualTo(10);
    }

    @DisplayName("토요일에 시작한 경우 독서 학습의 총 예상 기간을 계산한다.")
    @Test
    void calculateTotalExpectedPeriodWithReadingSaturday() {
        // given
        int planTimeInWeekDay = 30;
        int planTimeInWeekend = 60;
        int totalPage = 700;
        int readPagePerMin = 2;
        LocalDate startDate = LocalDate.of(2023, 9, 16);

        PeriodCalculator periodCalculator = createPeriodCalculator(planTimeInWeekDay, planTimeInWeekend, startDate);

        // when
        int period = periodCalculator.calculateExpectedPeriod(totalPage / readPagePerMin);

        // then
        assertThat(period).isEqualTo(9);
    }

    private PeriodCalculator createPeriodCalculator(int planTimeInWeekDay, int planTimeInWeekend, LocalDate startDate) {
        return PeriodCalculator.builder()
            .planMinutesInWeekday(planTimeInWeekDay)
            .planMinutesInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .build();
    }
}