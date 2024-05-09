package toyproject.studyscheduler.study.domain;

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
@Embeddable
public class StudyPlan {

    /**
     * 평일 학습 계획 시간
     */
    private int planMinutesInWeekday;

    /**
     * 주말 학습 계획 시간
     */
    private int planMinutesInWeekend;

    private StudyPlan(int planMinutesInWeekday, int planMinutesInWeekend) {
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
    }

    public static StudyPlan fromStarting(int planMinutesInWeekday, int planMinutesInWeekend) {
        return new StudyPlan(planMinutesInWeekday, planMinutesInWeekend);
    }

    public static StudyPlan fromTerminated() {
        return new StudyPlan(0, 0);
    }
}
