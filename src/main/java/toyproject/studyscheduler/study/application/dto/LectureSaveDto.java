package toyproject.studyscheduler.study.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.StudyInformation;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.domain.StudyPlan;
import toyproject.studyscheduler.study.domain.entity.Lecture;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@NoArgsConstructor
public class LectureSaveDto implements StudyDtoSpec {

    private String title;
    private String description;
    private boolean isTermination;
    private LocalDate startDate;
    private LocalDate endDate = LocalDate.MAX;
    private int planMinutesInWeekday;
    private int planMinutesInWeekend;

    @Getter
    private String teacherName;

    @Getter
    private int totalRuntime;

    @Builder
    private LectureSaveDto(String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend, String teacherName, int totalRuntime) {
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }

    @Override
    public Study toEntity() {
        StudyInformation studyInformation = StudyInformation.builder()
            .title(this.title)
            .description(this.description)
            .isTermination(this.isTermination)
            .build();

        StudyPeriod studyPeriod;
        StudyPlan studyPlan;
        if (isTermination) {
            studyPeriod = StudyPeriod.fromTerminated(this.startDate, this.endDate);
            studyPlan = StudyPlan.fromTerminated();
        } else {
            studyPeriod = StudyPeriod.fromStarting(this.startDate);
            studyPlan = StudyPlan.fromStarting(this.planMinutesInWeekday, this.planMinutesInWeekend);
        }

        return Lecture.builder()
            .studyInformation(studyInformation)
            .studyPeriod(studyPeriod)
            .studyPlan(studyPlan)
            .teacherName(this.teacherName)
            .totalRuntime(this.totalRuntime)
            .build();
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean isTermination() {
        return this.isTermination;
    }

    @Override
    public LocalDate getStartDate() {
        return this.startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return this.endDate;
    }

    @Override
    public int getPlanMinutesInWeekday() {
        return this.planMinutesInWeekday;
    }

    @Override
    public int getPlanMinutesInWeekend() {
        return this.planMinutesInWeekend;
    }
}
