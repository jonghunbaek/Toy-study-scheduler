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
        int period = 0;
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();
        int startDay = dayOfWeek.getValue();

        while (true) {
            if (totalRunTime <= 0) {
                break;
            }

            if (startDay <= FRIDAY.getValue()) {
                totalRunTime -= planTimeInWeekday;
                dayOfWeek = dayOfWeek.plus(1);
                startDay = dayOfWeek.getValue();
                period++;
                continue;
            }

            if (startDay <= SUNDAY.getValue()) {
                totalRunTime -= planTimeInWeekend;
                dayOfWeek = dayOfWeek.plus(1);
                startDay = dayOfWeek.getValue();
                period++;
            }
        }
        return period;
    }
}
