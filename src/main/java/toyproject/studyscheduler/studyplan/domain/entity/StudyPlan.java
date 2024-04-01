package toyproject.studyscheduler.studyplan.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StudyPlan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int planTimeInWeekday;
    private int planTimeInWeekend;
    private LocalDate expectedEndDate;

    @OneToOne
    private Study study;

    @Builder
    private StudyPlan(int planTimeInWeekday, int planTimeInWeekend, LocalDate expectedEndDate, Study study) {
        this.planTimeInWeekday = planTimeInWeekday;
        this.planTimeInWeekend = planTimeInWeekend;
        this.expectedEndDate = expectedEndDate;
        this.study = study;
    }
}
