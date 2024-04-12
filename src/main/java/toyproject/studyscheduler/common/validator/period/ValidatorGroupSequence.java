package toyproject.studyscheduler.common.validator.period;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({ Default.class, EndDateLaterThanStartDateGroup.class})
public interface ValidatorGroupSequence {
}
