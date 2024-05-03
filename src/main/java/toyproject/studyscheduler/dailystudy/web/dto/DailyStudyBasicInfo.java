package toyproject.studyscheduler.dailystudy.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class DailyStudyBasicInfo {

    private Long dailyStudyId;

    private int completeMinutesToday;

    private LocalDate studyDate;

    @Builder
    private DailyStudyBasicInfo(Long dailyStudyId, int completeMinutesToday, LocalDate studyDate) {
        this.dailyStudyId = dailyStudyId;
        this.completeMinutesToday = completeMinutesToday;
        this.studyDate = studyDate;
    }

    public static DailyStudyBasicInfo of(DailyStudy dailyStudy) {
        return DailyStudyBasicInfo.builder()
            .dailyStudyId(dailyStudy.getId())
            .completeMinutesToday(dailyStudy.getCompleteMinutesToday())
            .studyDate(dailyStudy.getStudyDate())
            .build();
    }
}
