package toyproject.studyscheduler.study.entity.domain;

import lombok.Builder;

public class StudyPlanningTime {

    private int planTimeInWeekday;
    private int planTimeInWeekend;

    @Builder
    private StudyPlanningTime(int planTimeInWeekday, int planTimeInWeekend) {
        this.planTimeInWeekday = planTimeInWeekday;
        this.planTimeInWeekend = planTimeInWeekend;
    }

    public static StudyPlanningTime from(int planTimeInWeekday, int planTimeInWeekend) {
        return new StudyPlanningTime(planTimeInWeekday, planTimeInWeekend);
    }
}
