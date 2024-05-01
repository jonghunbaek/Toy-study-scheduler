package toyproject.studyscheduler.dailystudy.web.dto;

import lombok.Builder;
import lombok.Getter;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;

import java.time.LocalDate;

@Getter
public class DailyStudyUpdateResult {

    private final String content;

    private final int completeMinutesToday;

    private final LocalDate studyDate;

    private final StudyRemaining studyRemaining;

    @Builder
    private DailyStudyUpdateResult(String content, int completeMinutesToday, LocalDate studyDate, StudyRemaining studyRemaining) {
        this.content = content;
        this.completeMinutesToday = completeMinutesToday;
        this.studyDate = studyDate;
        this.studyRemaining = studyRemaining;
    }

    public static DailyStudyUpdateResult from(DailyStudy dailyStudy, StudyRemaining studyRemaining) {
        return DailyStudyUpdateResult.builder()
            .content(dailyStudy.getContent())
            .completeMinutesToday(dailyStudy.getCompleteMinutesToday())
            .studyDate(dailyStudy.getStudyDate())
            .studyRemaining(studyRemaining)
            .build();
    }
}
