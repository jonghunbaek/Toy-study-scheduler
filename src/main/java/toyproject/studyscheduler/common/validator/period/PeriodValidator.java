package toyproject.studyscheduler.common.validator.period;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import toyproject.studyscheduler.study.application.dto.Period;

public class PeriodValidator implements ConstraintValidator<EndDateLaterThanStartDate, Period> {

    @Override
    public boolean isValid(Period value, ConstraintValidatorContext context) {
        return value.getStartDate().isBefore(value.getEndDate());
    }
}
