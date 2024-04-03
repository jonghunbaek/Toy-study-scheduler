package toyproject.studyscheduler.study.domain;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static java.time.DayOfWeek.*;

// TODO :: StudyUtil의 삭제에 따라 구조 변경 필요
@NoArgsConstructor
@Getter
public class PeriodCalculator {

    public static final int A_DAY_LATER = 1;
    private int planQuantityInWeekday;
    private int planQuantityInWeekend;
    private LocalDate startDate;

    @Builder
    public PeriodCalculator(int planQuantityInWeekday, int planQuantityInWeekend, LocalDate startDate) {
        this.planQuantityInWeekday = planQuantityInWeekday;
        this.planQuantityInWeekend = planQuantityInWeekend;
        this.startDate = startDate;
    }

    public int calculatePeriodBy(int totalRunTime) {
        return calculateExpectedPeriod(totalRunTime);
    }

    public int calculatePeriodBy(List<Integer> expectedTimes) {
        int totalExpectedTime = expectedTimes.stream()
            .mapToInt(i -> i)
            .sum();

        return calculateExpectedPeriod(totalExpectedTime);
    }

    // TODO : setUp과 마찬가지로 매개변수 2개의 타입이 같기 때문에 계산오류가 발생할 확률 높으니 리팩토링하자.
    public int calculatePeriodBy(int totalPage, int readPagePerMin) {
        planQuantityInWeekday *= readPagePerMin;
        planQuantityInWeekend *= readPagePerMin;

        return calculateExpectedPeriod(totalPage);
    }

    public int calculateExpectedPeriod(int studyQuantity) {
        int remainingQuantity = studyQuantity;
        DayOfWeek studyDayOfWeek = startDate.getDayOfWeek();
        int period = 0;

        while (remainingQuantity > 0) {
            remainingQuantity = calculateRemaining(remainingQuantity, studyDayOfWeek);

            period += A_DAY_LATER;
            studyDayOfWeek = studyDayOfWeek.plus(A_DAY_LATER);
        }

        return period;
    }

    private int calculateRemaining(int remainingQuantity, DayOfWeek studyDayOfWeek) {
        if (studyDayOfWeek.getValue() <= FRIDAY.getValue()) {
            return remainingQuantity - planQuantityInWeekday;
        }

        return remainingQuantity - planQuantityInWeekend;
    }
}
