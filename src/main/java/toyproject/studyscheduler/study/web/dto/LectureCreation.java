package toyproject.studyscheduler.study.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LectureCreation extends StudyCreation {

    private String teacherName;
    private int totalRuntime;

    @Builder
    private LectureCreation(String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, LocalDate expectedEndDate, int planMinutesInWeekday, int planMinutesInWeekend, String teacherName, int totalRuntime) {
        super(title, description, isTermination, startDate, endDate, expectedEndDate, planMinutesInWeekday, planMinutesInWeekend);
        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }
}
