package toyproject.studyscheduler.dailystudy.web.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class DailyStudyDetail {

    private Long dailyStudyId;

    private String content;

    private int completeMinutesToday;

    private LocalDate studyDate;

    private Long studyId;

    private String studyTitle;

    @Builder
    private DailyStudyDetail(Long dailyStudyId, String content, int completeMinutesToday, LocalDate studyDate, Long studyId, String studyTitle) {
        this.dailyStudyId = dailyStudyId;
        this.content = content;
        this.completeMinutesToday = completeMinutesToday;
        this.studyDate = studyDate;
        this.studyId = studyId;
        this.studyTitle = studyTitle;
    }

    public static DailyStudyDetail of(DailyStudy dailyStudy) {
        Study study = dailyStudy.getStudy();

        return DailyStudyDetail.builder()
            .dailyStudyId(dailyStudy.getId())
            .content(dailyStudy.getContent())
            .completeMinutesToday(dailyStudy.getCompleteMinutesToday())
            .studyDate(dailyStudy.getStudyDate())
            .studyId(study.getId())
            .studyTitle(study.getStudyInformation().getTitle())
            .build();
    }
}
