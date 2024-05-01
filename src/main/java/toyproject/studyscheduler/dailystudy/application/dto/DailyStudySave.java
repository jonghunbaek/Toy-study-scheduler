package toyproject.studyscheduler.dailystudy.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class DailyStudySave {

    @NotNull(message = "학습 id는 필수 값입니다.")
    @Min(value = 1, message = "학습 id의 최소 값은 1입니다.")
    private Long studyId;

    @Size(min = 0, max = 1000, message = "학습 내용은 1000자 이하이어야 합니다.")
    private String content;

    @Min(value = 1, message = "최소 학습 시간은 1분입니다.")
    private int completeMinutesToday;

    @NotNull(message = "학습일은 필수 입력 값입니다.")
    private LocalDate studyDate;

    @Builder
    private DailyStudySave(Long studyId, String content, int completeMinutesToday, LocalDate studyDate) {
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
