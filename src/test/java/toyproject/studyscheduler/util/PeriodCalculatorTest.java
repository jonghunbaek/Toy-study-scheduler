package toyproject.studyscheduler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        PeriodCalculator calculator = PeriodCalculator.builder()
                .planQuantityInWeekday(planTimeInWeekDay)
                .planQuantityInWeekend(planTimeInWeekend)
                .startDate(startDate)
                .build();

        // when
        int period = calculator.calculatePeriodBy(totalRunTime);

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

        PeriodCalculator calculator = PeriodCalculator.builder()
            .planQuantityInWeekday(planTimeInWeekDay)
            .planQuantityInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .build();

        // when
        int period = calculator.calculatePeriodBy(totalPage, readPagePerMin);

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

        PeriodCalculator calculator = PeriodCalculator.builder()
            .planQuantityInWeekday(planTimeInWeekDay)
            .planQuantityInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .build();

        // when
        int period = calculator.calculatePeriodBy(totalPage, readPagePerMin);

        // then
        assertThat(period).isEqualTo(9);
    }

    @DisplayName("월요일에 시작한 경우 토이프로젝트 학습의 총 예상 기간을 계산한다.")
    @Test
    void calculateTotalExpectedPeriodWithToyProjectMonday() {
        // given
        int planTimeInWeekDay = 90;
        int planTimeInWeekend = 180;
        List<Integer> expectedTimes = List.of(300, 600, 250, 100, 500);
        LocalDate startDate = LocalDate.of(2023, 9, 11);

        PeriodCalculator calculator = PeriodCalculator.builder()
            .planQuantityInWeekday(planTimeInWeekDay)
            .planQuantityInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .build();

        // when
        int period = calculator.calculatePeriodBy(expectedTimes);

        // then
        assertThat(period).isEqualTo(16);
    }

    @DisplayName("토요일에 시작한 경우 토이프로젝트 학습의 총 예상 기간을 계산한다.")
    @Test
    void calculateTotalExpectedPeriodWithToyProjectSaturday() {
        // given
        int planTimeInWeekDay = 90;
        int planTimeInWeekend = 180;
        List<Integer> expectedTimes = List.of(300, 600, 250, 100, 500);
        LocalDate startDate = LocalDate.of(2023, 9, 15);

        PeriodCalculator calculator = PeriodCalculator.builder()
            .planQuantityInWeekday(planTimeInWeekDay)
            .planQuantityInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .build();

        // when
        int period = calculator.calculatePeriodBy(expectedTimes);

        // then
        assertThat(period).isEqualTo(16);
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