package toyproject.studyscheduler.study.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.study.domain.entity.Lecture;
import toyproject.studyscheduler.study.domain.entity.Reading;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

import static toyproject.studyscheduler.study.domain.StudyPeriod.*;

@Getter
@NoArgsConstructor
public abstract class StudyDetail {

    private String title;
    private String description;
    private boolean isTermination;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate expectedEndDate;
    private int planMinutesInWeekday;
    private int planMinutesInWeekend;

    protected StudyDetail(String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, LocalDate expectedEndDate, int planMinutesInWeekday, int planMinutesInWeekend) {
        this.title = title;
        this.description = description;
        this.isTermination = isTermination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.expectedEndDate = expectedEndDate;
        this.planMinutesInWeekday = planMinutesInWeekday;
        this.planMinutesInWeekend = planMinutesInWeekend;
    }

    public static StudyDetail of(Study study) {
        String title = study.getStudyInformation().getTitle();
        String description = study.getStudyInformation().getDescription();
        boolean isTermination = study.getStudyInformation().isTermination();
        LocalDate startDate = study.getStudyPeriod().getStartDate();
        LocalDate endDate = study.getStudyPeriod().getEndDate();
        int planMinutesInWeekday = study.getStudyPlan().getPlanMinutesInWeekday();
        int planMinutesInWeekend = study.getStudyPlan().getPlanMinutesInWeekend();

        LocalDate expectedEndDate = TEMP_END_DATE;
        if (!isTermination) {
            expectedEndDate = study.calculateExpectedDate();
        }

        if (study instanceof Lecture) {
            return createLecture((Lecture) study, title, description, isTermination, startDate, endDate, planMinutesInWeekday, planMinutesInWeekend, expectedEndDate);
        }

        return createReading((Reading) study, title, description, isTermination, startDate, endDate, planMinutesInWeekday, planMinutesInWeekend, expectedEndDate);
    }

    private static ReadingDetail createReading(Reading reading, String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend, LocalDate expectedEndDate) {
        return ReadingDetail.builder()
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

    private static LectureDetail createLecture(Lecture lecture, String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, int planMinutesInWeekday, int planMinutesInWeekend, LocalDate expectedEndDate) {
        return LectureDetail.builder()
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
}
