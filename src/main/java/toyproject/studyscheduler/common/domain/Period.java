package toyproject.studyscheduler.common.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import toyproject.studyscheduler.common.validator.EndDateLaterThanStartDate;
import toyproject.studyscheduler.common.validator.EndDateLaterThanStartDateGroup;
import toyproject.studyscheduler.study.domain.StudyPeriod;

import java.time.LocalDate;

@EndDateLaterThanStartDate(groups = EndDateLaterThanStartDateGroup.class)
@Getter
public class Period {

    // TODO :: 공통 도메인으로 옮기면서 Not Null검증 불필요해짐. 추후 StudyController변경하면서 같이 변경
    @NotNull(message = "시작일은 필수 입력 값입니다.")
    private final LocalDate startDate;

    @NotNull(message = "종료일은 필수 입력 값입니다.")
    private final LocalDate endDate;

    public Period(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Period of(StudyPeriod studyPeriod) {
        return new Period(studyPeriod.getStartDate(), studyPeriod.getEndDate());
    }
}
