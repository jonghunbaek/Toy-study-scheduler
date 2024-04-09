package toyproject.studyscheduler.study.web.dto;

import lombok.Getter;
import toyproject.studyscheduler.study.application.dto.Period;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@Getter
public class StudyInAction {

    private String title;
    private Period period;

    private StudyInAction(String title, Period period) {
        this.title = title;
        this.period = period;
    }

    public static StudyInAction of(Study study) {
        Period period = Period.of(study.getStudyPeriod());

        return new StudyInAction(study.getStudyInformation().getTitle(), period);
    }
}
