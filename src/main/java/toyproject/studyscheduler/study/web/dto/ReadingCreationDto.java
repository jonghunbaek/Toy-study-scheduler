package toyproject.studyscheduler.study.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReadingCreationDto extends StudyCreationSpec {

    private String authorName;
    private int totalPage;
    private int readPagePerMin;

    @Builder
    private ReadingCreationDto(String title, String description, boolean isTermination, LocalDate startDate, LocalDate endDate, LocalDate expectedEndDate, int planMinutesInWeekday, int planMinutesInWeekend, String authorName, int totalPage, int readPagePerMin) {
        super(title, description, isTermination, startDate, endDate, expectedEndDate, planMinutesInWeekday, planMinutesInWeekend);
        this.authorName = authorName;
        this.totalPage = totalPage;
        this.readPagePerMin = readPagePerMin;
    }
}
