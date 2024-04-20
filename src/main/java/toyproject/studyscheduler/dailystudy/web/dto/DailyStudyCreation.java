package toyproject.studyscheduler.dailystudy.web.dto;

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

    private DailyStudyCreation(Long dailyStudyId, String content, int completeMinutesToday, LocalDate studyDate, boolean isTermination) {
        this.dailyStudyId = dailyStudyId;
        this.content = content;
        this.completeMinutesToday = completeMinutesToday;
        this.studyDate = studyDate;
        this.isTermination = isTermination;
    }

    public static DailyStudyCreation of(DailyStudy dailyStudy, boolean isTermination) {
        return new DailyStudyCreation(dailyStudy.getId(), dailyStudy.getContent(), dailyStudy.getCompleteMinutesToday(), dailyStudy.getStudyDate(), isTermination);
    }
}
