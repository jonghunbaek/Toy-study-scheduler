package toyproject.studyscheduler.study.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toyproject.studyscheduler.common.validator.EndDateLaterThanStartDate;
import toyproject.studyscheduler.study.domain.StudyPeriod;
import toyproject.studyscheduler.study.exception.StudyException;

import java.time.LocalDate;

import static toyproject.studyscheduler.common.response.ResponseCode.E30001;

@EndDateLaterThanStartDate
@NoArgsConstructor
@Getter
@Setter
public class Period {

    @NotNull(message = "시작일은 필수 입력 값입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수 입력 값입니다.")
    private LocalDate endDate;

    public Period(LocalDate startDate, LocalDate endDate) {
        validateStartDtEarlierEndDt(startDate, endDate);

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Period of(StudyPeriod studyPeriod) {
        return new Period(studyPeriod.getStartDate(), studyPeriod.getEndDate());
    }

    private void validateStartDtEarlierEndDt(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new StudyException("startDate, endDate :: " + startDate + ", " + endDate, E30001);
        }
    }
}
