package toyproject.studyscheduler.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import toyproject.studyscheduler.common.domain.Period;

public class PeriodValidator implements ConstraintValidator<EndDateLaterThanStartDate, Period> {

    @Override
    public boolean isValid(Period value, ConstraintValidatorContext context) {
        return value.getStartDate().isBefore(value.getEndDate());
    }
}
