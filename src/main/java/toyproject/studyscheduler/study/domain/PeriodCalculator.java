package toyproject.studyscheduler.study.domain;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static java.time.DayOfWeek.*;

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
