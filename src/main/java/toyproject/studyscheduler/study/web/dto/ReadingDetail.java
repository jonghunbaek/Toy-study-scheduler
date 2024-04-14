package toyproject.studyscheduler.study.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReadingDetail extends StudyDetail {

    private String authorName;
    private int totalPage;
    private int readPagePerMin;

    @Builder
    private ReadingDetail(Long studyId, String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, LocalDate expectedEndDate, int planMinutesInWeekday, int planMinutesInWeekend, String authorName, int totalPage, int readPagePerMin) {
        super(studyId, title, description, isTermination, startDate, endDate, expectedEndDate, planMinutesInWeekday, planMinutesInWeekend);
        this.authorName = authorName;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
    }
}
