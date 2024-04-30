package toyproject.studyscheduler.dailystudy.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailyStudyUpdateResult {

    private int remainingStudyMinutes;
    private LocalDate expectedEndDate;
    private boolean isTermination;

    public DailyStudyUpdateResult(int remainingStudyMinutes, LocalDate expectedEndDate, boolean isTermination) {
        this.remainingStudyMinutes = remainingStudyMinutes;
        this.expectedEndDate = expectedEndDate;
        this.isTermination = isTermination;
    }
}
