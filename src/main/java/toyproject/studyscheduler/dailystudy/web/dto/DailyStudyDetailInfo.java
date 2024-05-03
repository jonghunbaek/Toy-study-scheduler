package toyproject.studyscheduler.dailystudy.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class DailyStudyDetailInfo {

    private Long dailyStudyId;

    private String content;

    private int completeMinutesToday;

    private LocalDate studyDate;

    private Long studyId;

    private String studyTitle;

    @Builder
    private DailyStudyDetailInfo(Long dailyStudyId, String content, int completeMinutesToday, LocalDate studyDate, Long studyId, String studyTitle) {
        this.dailyStudyId = dailyStudyId;
        this.content = content;
        this.completeMinutesToday = completeMinutesToday;
        this.studyDate = studyDate;
        this.studyId = studyId;
        this.studyTitle = studyTitle;
    }

    public static DailyStudyDetailInfo of(DailyStudy dailyStudy) {
        Study study = dailyStudy.getStudy();

        return DailyStudyDetailInfo.builder()
            .dailyStudyId(dailyStudy.getId())
            .content(dailyStudy.getContent())
            .completeMinutesToday(dailyStudy.getCompleteMinutesToday())
            .studyDate(dailyStudy.getStudyDate())
            .studyId(study.getId())
            .studyTitle(study.getStudyInformation().getTitle())
            .build();
    }
}
