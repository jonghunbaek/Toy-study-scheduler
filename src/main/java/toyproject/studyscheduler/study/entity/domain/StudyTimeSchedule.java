package toyproject.studyscheduler.study.entity.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Embeddable
public class StudyTimeSchedule {

    private StudyPlanningTime studyPlanningTime;
    private LocalDate startDate;
    private LocalDate planEndDate;
    private LocalDate realEndDate;

    @Builder
    private StudyTimeSchedule(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate, LocalDate planEndDate, LocalDate realEndDate) {
        this.studyPlanningTime = StudyPlanningTime.from(planTimeInWeekday, planTimeInWeekend);
        this.startDate = startDate;
        this.planEndDate = planEndDate;
        this.realEndDate = realEndDate;
    }

    public static StudyTimeSchedule fromTerminatedStudy(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate, LocalDate realEndDate) {
        return StudyTimeSchedule.builder()
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .realEndDate(realEndDate)
            .build();
    }

    public static StudyTimeSchedule fromStartingStudy(int planTimeInWeekday, int planTimeInWeekend, LocalDate startDate, LocalDate planEndDate) {
        return StudyTimeSchedule.builder()
            .planTimeInWeekday(planTimeInWeekday)
            .planTimeInWeekend(planTimeInWeekend)
            .startDate(startDate)
            .planEndDate(planEndDate)
            .build();
    }
}
