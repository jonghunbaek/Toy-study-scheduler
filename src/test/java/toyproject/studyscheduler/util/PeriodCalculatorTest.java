package toyproject.studyscheduler.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import toyproject.studyscheduler.domain.function.FunctionType;
import toyproject.studyscheduler.domain.function.RequiredFunction;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static toyproject.studyscheduler.domain.function.FunctionType.*;

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
        Assertions.assertThat(period).isEqualTo(8);
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
        Assertions.assertThat(period).isEqualTo(10);
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
        Assertions.assertThat(period).isEqualTo(9);
    }

    @DisplayName("월요일에 시작한 경우 토이프로젝트 학습의 총 예상 기간을 계산한다.")
    @Test
    void calculateTotalExpectedPeriodWithToyProjectMonday() {
        // given
        int planTimeInWeekDay = 90;
        int planTimeInWeekend = 180;

        RequiredFunction function1 = createFunction("1번기능", "1번 기능 내용", 300, CREATE);
        RequiredFunction function2 = createFunction("2번기능", "2번 기능 내용", 600, READ);
        RequiredFunction function3 = createFunction("3번기능", "3번 기능 내용", 250, UPDATE);
        RequiredFunction function4 = createFunction("4번기능", "4번 기능 내용", 100, DELETE);
        RequiredFunction function5 = createFunction("5번기능", "5번 기능 내용", 500, ETC);

        LocalDate startDate = LocalDate.of(2023, 9, 11);

        PeriodCalculator calculator = PeriodCalculator.builder()
            .planQuantityInWeekday(planTimeInWeekDay)
            .planQuantityInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .build();

        // when
        int period = calculator.calculatePeriodBy(List.of(function1, function2, function3, function4, function5));

        // then
        Assertions.assertThat(period).isEqualTo(16);
    }

    @DisplayName("토요일에 시작한 경우 토이프로젝트 학습의 총 예상 기간을 계산한다.")
    @Test
    void calculateTotalExpectedPeriodWithToyProjectSaturday() {
        // given
        int planTimeInWeekDay = 90;
        int planTimeInWeekend = 180;

        RequiredFunction function1 = createFunction("1번기능", "1번 기능 내용", 300, CREATE);
        RequiredFunction function2 = createFunction("2번기능", "2번 기능 내용", 600, READ);
        RequiredFunction function3 = createFunction("3번기능", "3번 기능 내용", 250, UPDATE);
        RequiredFunction function4 = createFunction("4번기능", "4번 기능 내용", 100, DELETE);
        RequiredFunction function5 = createFunction("5번기능", "5번 기능 내용", 500, ETC);

        LocalDate startDate = LocalDate.of(2023, 9, 15);

        PeriodCalculator calculator = PeriodCalculator.builder()
            .planQuantityInWeekday(planTimeInWeekDay)
            .planQuantityInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .build();

        // when
        int period = calculator.calculatePeriodBy(List.of(function1, function2, function3, function4, function5));

        // then
        Assertions.assertThat(period).isEqualTo(16);
    }

    private RequiredFunction createFunction(String title, String description, int expectedTime, FunctionType functionType) {
        return RequiredFunction.builder()
            .title(title)
            .description(description)
            .expectedTime(expectedTime)
            .functionType(functionType)
            .build();
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