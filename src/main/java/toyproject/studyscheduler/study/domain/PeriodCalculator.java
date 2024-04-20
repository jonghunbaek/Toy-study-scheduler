package toyproject.studyscheduler.study.domain;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static java.time.DayOfWeek.*;

@NoArgsConstructor
@Getter
public class PeriodCalculator {

    public static final int A_DAY_LATER = 1;
    private int planMinutesInWeekday;
    private int planMinutesInWeekend;
    private LocalDate startDate;

    @Builder
    private PeriodCalculator(int planMinutesInWeekday, int planMinutesInWeekend, LocalDate startDate) {
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
        this.startDate = startDate;
    }

    public static PeriodCalculator from(StudyPlan studyPlan, LocalDate startDate) {
        return new PeriodCalculator(studyPlan.getPlanMinutesInWeekday(), studyPlan.getPlanMinutesInWeekend(), startDate);
    }

    public int calculateExpectedPeriod(int totalMinutes) {
        int remainingMinutes = totalMinutes;
        DayOfWeek studyDayOfWeek = startDate.getDayOfWeek();
        int period = 0;

        while (remainingMinutes > 0) {
            remainingMinutes = calculateRemaining(remainingMinutes, studyDayOfWeek);

            period += A_DAY_LATER;
            studyDayOfWeek = studyDayOfWeek.plus(A_DAY_LATER);
        }

        return period;
    }

    private int calculateRemaining(int remainingMinutes, DayOfWeek studyDayOfWeek) {
        if (studyDayOfWeek.getValue() <= FRIDAY.getValue()) {
            return remainingMinutes - planMinutesInWeekday;
        }

        return remainingMinutes - planMinutesInWeekend;
    }
}
