package toyproject.studyscheduler.dailystudy.web.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RemainingStudyDays {

    private LocalDate expectedEndDate;
    private int remainingDays;

    public RemainingStudyDays(LocalDate expectedEndDate, int remainingDays) {
        this.expectedEndDate = expectedEndDate;
        this.remainingDays = remainingDays;
    }
}
