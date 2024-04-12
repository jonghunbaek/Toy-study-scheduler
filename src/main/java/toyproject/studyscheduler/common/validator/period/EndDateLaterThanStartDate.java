package toyproject.studyscheduler.common.validator.period;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PeriodValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndDateLaterThanStartDate {

    String message() default "종료일은 시작일보다 나중이어야 합니다.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
