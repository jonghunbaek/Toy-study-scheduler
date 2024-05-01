package toyproject.studyscheduler.dailystudy.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;

import java.time.LocalDate;

@Getter
public class DailyStudyUpdate {

    @Size(max = 1000, message = "학습 내용은 1000자 이하이어야 합니다.")
    private final String content;

    @Min(value = 1, message = "최소 학습 시간은 1분입니다.")
    private final int completeMinutesToday;

    @NotNull(message = "학습일은 필수 입력 값입니다.")
    private final LocalDate studyDate;

    @Builder
    private DailyStudyUpdate(String content, int completeMinutesToday, LocalDate studyDate) {
        this.content = content;
        this.completeMinutesToday = completeMinutesToday;
        this.studyDate = studyDate;
    }

    public void update(DailyStudy dailyStudy) {
        dailyStudy.updateDailyStudy(content, completeMinutesToday, studyDate);
    }
}
