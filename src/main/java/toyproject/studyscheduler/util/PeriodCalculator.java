package toyproject.studyscheduler.util;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static java.time.DayOfWeek.*;

/**
 *  예상되는 학습 기간을 계산하는 클래스. StudyUtil에 의해서만 호출된다.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PeriodCalculator {

    private int planTimeInWeekday;
    private int planTimeInWeekend;
    private LocalDate startDate;

    @Builder(access = AccessLevel.PROTECTED)
    private PeriodCalculator(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate) {
        this.planTimeInWeekday = planTimeInWeekday;
        this.planTimeInWeekend = planTimeInWeekend;
        this.startDate = startDate;
    }

    public int calculateTotalExpectedPeriod(int totalRunTime) {
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();
        int period = 0;
        int startDay = 0;

        while (true) {
            startDay = dayOfWeek.getValue();
            period++;

            if (startDay <= FRIDAY.getValue()) {
                totalRunTime = calculateRemaining(totalRunTime, planTimeInWeekday);
            } else {
                totalRunTime = calculateRemaining(totalRunTime, planTimeInWeekend);
            }

            if (totalRunTime <= 0) {
                break;
            }

            dayOfWeek = dayOfWeek.plus(1);
        }

        return period;
    }

    private int calculateRemaining(int total, int planTime) {
        return total - planTime;
    }
}
