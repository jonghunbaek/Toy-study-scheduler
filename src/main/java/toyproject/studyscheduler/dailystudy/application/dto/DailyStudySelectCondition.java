package toyproject.studyscheduler.dailystudy.application.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import toyproject.studyscheduler.common.domain.Period;

@Getter
public class DailyStudySelectCondition {

    @NotNull(message = "id값은 필수 값입니다.")
    @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.")
    private Long studyId;
    private Period period;

    public DailyStudySelectCondition(Long studyId, Period period) {
        this.studyId = studyId;
        this.period = period;
    }
}
