package toyproject.studyscheduler.study.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LectureDetail extends StudyDetail {

    private final String teacherName;
    private final int totalRuntime;

    @Builder
    private LectureDetail(Long studyId, String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, LocalDate expectedEndDate, int planMinutesInWeekday, int planMinutesInWeekend, String teacherName, int totalRuntime) {
        super(studyId, title, description, isTermination, startDate, endDate, expectedEndDate, planMinutesInWeekday, planMinutesInWeekend);
        this.teacherName = teacherName;
        this.totalRuntime = totalRuntime;
    }
}
