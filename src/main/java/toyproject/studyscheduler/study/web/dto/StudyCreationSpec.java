package toyproject.studyscheduler.study.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.entity.Lecture;
import toyproject.studyscheduler.study.domain.entity.Reading;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public abstract class StudyCreationSpec {

    private String title;
    private String description;
    private boolean isTermination;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate expectedEndDate;
    private int planMinutesInWeekday;
    private int planMinutesInWeekend;

    protected StudyCreationSpec(String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, LocalDate expectedEndDate, int planMinutesInWeekday, int planMinutesInWeekend) {
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.expectedEndDate = expectedEndDate;
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
    }

    public static StudyCreationSpec of(Study study) {
        String title = study.getStudyInformation().getTitle();
        String description = study.getStudyInformation().getDescription();
        boolean isTermination = study.getStudyInformation().isTermination();
        LocalDate startDate = study.getStudyPeriod().getStartDate();
        LocalDate endDate = study.getStudyPeriod().getEndDate();
        int planMinutesInWeekday = study.getStudyPlan().getPlanMinutesInWeekday();
        int planMinutesInWeekend = study.getStudyPlan().getPlanMinutesInWeekend();

        LocalDate expectedEndDate = LocalDate.MAX;
        if (isTermination) {
            expectedEndDate = study.calculateExpectedDate();
        }

        if (study instanceof Lecture) {
            Lecture lecture = (Lecture) study;
            return LectureCreationDto.builder()
                .title(title)
                .description(description)
                .isTermination(isTermination)
                .startDate(startDate)
                .endDate(endDate)
                .expectedEndDate(expectedEndDate)
                .planMinutesInWeekday(planMinutesInWeekday)
                .planMinutesInWeekend(planMinutesInWeekend)
                .totalRuntime(lecture.getTotalRuntime())
                .teacherName(lecture.getTeacherName())
                .build();
        }

        Reading reading = (Reading) study;
        return ReadingCreationDto.builder()
            .title(title)
            .description(description)
            .isTermination(isTermination)
            .startDate(startDate)
            .endDate(endDate)
            .expectedEndDate(expectedEndDate)
            .planMinutesInWeekday(planMinutesInWeekday)
            .planMinutesInWeekend(planMinutesInWeekend)
            .totalPage(reading.getTotalPage())
            .authorName(reading.getAuthorName())
            .readPagePerMin(reading.getReadPagePerMin())
            .build();
    }
}
