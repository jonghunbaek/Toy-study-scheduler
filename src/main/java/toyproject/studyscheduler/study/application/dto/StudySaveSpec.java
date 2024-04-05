package toyproject.studyscheduler.study.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

public abstract class StudySaveSpec {

    @NotBlank
    private String title;

    @NotBlank
    @Max(100)
    private String description;

    private boolean isTermination;

    @NotBlank
    private LocalDate startDate;

    private LocalDate endDate = LocalDate.MAX;

    @NotBlank
    @Size(min = 1, max = 720)
    private int planMinutesInWeekday;

    @NotBlank
    @Size(min = 1, max = 720)
    private int planMinutesInWeekend;

    protected StudySaveSpec(String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend) {
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
    }

    public Study toStudy() {
        StudyInformation information = createStudyInfo();
        StudyPeriod period = createStudyPeriod();
        StudyPlan plan = createStudyPlan();

        return createStudy(information, period, plan);
    }

    protected abstract Study createStudy(StudyInformation information, StudyPeriod period, StudyPlan plan);

    private StudyInformation createStudyInfo() {
        return StudyInformation.builder()
            .title(this.title)
            .description(this.description)
            .isTermination(this.isTermination)
            .build();
    }

    private StudyPeriod createStudyPeriod() {
        if (isTermination) {
            return StudyPeriod.fromTerminated(this.startDate, this.endDate);
        }

        return StudyPeriod.fromStarting(this.startDate);
    }

    private StudyPlan createStudyPlan() {
        if (isTermination) {
            return StudyPlan.fromTerminated();
        }

        return StudyPlan.fromStarting(this.planMinutesInWeekday, this.planMinutesInWeekend);
    }
}
