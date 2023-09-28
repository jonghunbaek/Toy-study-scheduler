package toyproject.studyscheduler.util;

import lombok.Getter;

@Getter
public class StudyUtil {

    private final static int WEEKDAYS = 5;
    private final static int WEEKENDS = 2;
    public int calculateTotalExpectedPeriod(int planTimeInWeekday, int planTimeInWeekend, int totalRunTime) {
        int totalTimeInWeekDays = planTimeInWeekday * WEEKDAYS;
        int totalTimeInWeekend = planTimeInWeekend * WEEKDAYS;

        return 8;
    }
}
