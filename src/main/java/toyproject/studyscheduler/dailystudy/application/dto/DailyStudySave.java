package toyproject.studyscheduler.dailystudy.application.dto;

import lombok.Getter;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@Getter
public class DailyStudySave {

    private Long studyId;

    private String content;

    private int completeMinutesToday;

    private LocalDate studyDate;

    public DailyStudySave(Long studyId, String content, int completeMinutesToday, LocalDate studyDate) {
        this.studyId = studyId;
        this.content = content;
        this.completeMinutesToday = completeMinutesToday;
        this.studyDate = studyDate;
    }

    public DailyStudy toEntity(Study study) {
        return DailyStudy.builder()
            .content(content)
            .completeMinutesToday(completeMinutesToday)
            .studyDate(studyDate)
            .study(study)
            .build();
    }
}
