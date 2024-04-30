package toyproject.studyscheduler.dailystudy.web.dto;

import lombok.Builder;
import lombok.Getter;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;

import java.time.LocalDate;

@Getter
public class DailyStudyCreation {

    private Long dailyStudyId;

    private String content;

    private int completeMinutesToday;

    private LocalDate studyDate;

    private boolean isTermination;

    private LocalDate expectedEndDate;

    private int remainingStudyMinutes;

    @Builder
    private DailyStudyCreation(Long dailyStudyId, String content, int completeMinutesToday, LocalDate studyDate, boolean isTermination, LocalDate expectedEndDate, int remainingStudyMinutes) {
        this.dailyStudyId = dailyStudyId;
        this.content = content;
        this.completeMinutesToday = completeMinutesToday;
        this.studyDate = studyDate;
        this.isTermination = isTermination;
        this.expectedEndDate = expectedEndDate;
        this.remainingStudyMinutes = remainingStudyMinutes;
    }

    public static DailyStudyCreation from(DailyStudy dailyStudy, boolean isTermination, LocalDate expectedEndDate, int remainingStudyMinutes) {
        return DailyStudyCreation.builder()
                .dailyStudyId(dailyStudy.getId())
                .content(dailyStudy.getContent())
                .completeMinutesToday(dailyStudy.getCompleteMinutesToday())
                .studyDate(dailyStudy.getStudyDate())
                .isTermination(isTermination)
                .expectedEndDate(expectedEndDate)
                .remainingStudyMinutes(remainingStudyMinutes)
                .build();
    }
}
