package toyproject.studyscheduler.studyplan.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static java.time.DayOfWeek.FRIDAY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StudyPlan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int planMinutesInWeekday;
    private int planMinutesInWeekend;
    private LocalDate expectedEndDate;

    @OneToOne
    private Study study;

    @Builder
    private StudyPlan(int planMinutesInWeekday, int planMinutesInWeekend, LocalDate expectedEndDate, Study study) {
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
        this.expectedEndDate = expectedEndDate;
        this.study = study;
    }

    public static StudyPlan from(int planMinutesInWeekday, int planMinutesInWeekend, Study study) {
        LocalDate expectedDate = study.getStartDate()
            .plusDays(calculateExpectedPeriod(study, planMinutesInWeekday, planMinutesInWeekend));

        return new StudyPlan(planMinutesInWeekday, planMinutesInWeekend, expectedDate, study);
    }

    private static int calculateExpectedPeriod(Study study, int planMinutesInWeekday, int planMinutesInWeekend) {
        int totalQuantity = study.getTotalQuantity();
        DayOfWeek dayOfWeek = study.getStartDate().getDayOfWeek();
        int period = 0;
        int studyDay = 0;
        int planQuantityInWeekday = study.calculatePlanQuantityPerDay(planMinutesInWeekday);
        int planQuantityInWeekend = study.calculatePlanQuantityPerDay(planMinutesInWeekend);

        while (true) {
            studyDay = dayOfWeek.getValue();
            period++;

            totalQuantity = calculateRemaining(totalQuantity, studyDay, planQuantityInWeekday, planQuantityInWeekend);

            if (totalQuantity <= 0) {
                break;
            }

            dayOfWeek = dayOfWeek.plus(1);
        }

        return period;
    }

    private static int calculateRemaining(int totalQuantity, int studyDay, int planQuantityInWeekday, int planQuantityInWeekend) {
        if (studyDay <= FRIDAY.getValue()) {
            return totalQuantity - planQuantityInWeekday;
        }

        return totalQuantity - planQuantityInWeekend;
    }
}
