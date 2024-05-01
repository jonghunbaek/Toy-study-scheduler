package toyproject.studyscheduler.study.web.dto;

import lombok.Getter;
import toyproject.studyscheduler.study.application.dto.Period;
import toyproject.studyscheduler.study.domain.entity.Study;

@Getter
public class StudyInAction {

    private final long studyId;
    private final String title;
    private final Period period;

    private StudyInAction(long studyId, String title, Period period) {
        this.studyId = studyId;
        this.title = title;
        this.period = period;
    }

    public static StudyInAction of(Study study) {
        Period period = Period.of(study.getStudyPeriod());

        return new StudyInAction(study.getId(), study.getStudyInformation().getTitle(), period);
    }
}
