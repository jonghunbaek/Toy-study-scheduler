package toyproject.studyscheduler.study.application.dto;

import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

public abstract class StudySaveSpec {

    private String title;
    private String description;
    private boolean isTermination;
    private LocalDate startDate;
    private LocalDate endDate = LocalDate.MAX;
    private int planMinutesInWeekday;
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
