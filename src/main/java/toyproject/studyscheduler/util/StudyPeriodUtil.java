package toyproject.studyscheduler.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static java.time.DayOfWeek.*;

@Component
@Getter
public class StudyPeriodUtil {

    private int planTimeInWeekday;
    private int planTimeInWeekend;
    private LocalDate startDate;

    public StudyPeriodUtil setUpBasicInfo(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate) {
        this.planTimeInWeekday = planTimeInWeekday;
        this.planTimeInWeekend = planTimeInWeekend;
        this.startDate = startDate;
        return this;
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
                startDay = dayOfWeek.plus(1).getValue();
                period++;
                continue;
            }

            if (FRIDAY.getValue() < startDay && startDay <= SUNDAY.getValue()) {
                startDay = dayOfWeek.plus(1).getValue();
                period++;
                continue;
            }
        }

        return period;
    }
}
