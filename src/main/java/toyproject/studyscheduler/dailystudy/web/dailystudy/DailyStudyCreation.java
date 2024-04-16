package toyproject.studyscheduler.dailystudy.web.dailystudy;

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

    private DailyStudyCreation(Long dailyStudyId, String content, int completeMinutesToday, LocalDate studyDate) {
        this.dailyStudyId = dailyStudyId;
        this.content = content;
        this.completeMinutesToday = completeMinutesToday;
        this.studyDate = studyDate;
    }

    public static DailyStudyCreation of(DailyStudy dailyStudy) {
        return new DailyStudyCreation(dailyStudy.getId(), dailyStudy.getContent(), dailyStudy.getCompleteMinutesToday(), dailyStudy.getStudyDate());
    }
}
