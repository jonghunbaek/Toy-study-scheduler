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
        int studyDay = 0;

        while (true) {
            studyDay = dayOfWeek.getValue();
            period++;

            totalRunTime = calculateRemaining(totalRunTime, studyDay);

            if (totalRunTime <= 0) {
                break;
            }

            dayOfWeek = dayOfWeek.plus(1);
        }

        return period;
    }

    public int calculateTotalExpectedPeriod(int totalPage, int readPagePerMin) {
        DayOfWeek dayOfWeek = startDate.getDayOfWeek();
        int period = 0;
        int studyDay = 0;

        while (true) {
            studyDay = dayOfWeek.getValue();
            period++;

            totalPage = calculateRemaining(totalPage, studyDay, readPagePerMin);

            if (totalPage <= 0) {
                break;
            }

            dayOfWeek = dayOfWeek.plus(1);
        }

        return period;
    }

    private int calculateRemaining(int totalRunTime, int studyDay) {
        if (studyDay <= FRIDAY.getValue()) {
            return totalRunTime - planTimeInWeekday;
        } else {
            return totalRunTime - planTimeInWeekend;
        }
    }

    private int calculateRemaining(int totalPage, int studyDay, int readPagePerMin) {
        if (studyDay <= FRIDAY.getValue()) {
            return totalPage - (planTimeInWeekday * readPagePerMin);
        } else {
            return totalPage - (planTimeInWeekend * readPagePerMin);
        }
    }
}
