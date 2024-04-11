package toyproject.studyscheduler.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import toyproject.studyscheduler.study.application.dto.Period;

import java.time.LocalDate;

public class PeriodValidator implements ConstraintValidator<EndDateLaterThanStartDate, Period> {

    @Override
    public void initialize(EndDateLaterThanStartDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Period value, ConstraintValidatorContext context) {
        // TODO :: start, endDate에 대한 입력 값 검증이 이루어지지 않은 상태로 넘어옴
        return value.getStartDate().isBefore(value.getEndDate());
    }
}
