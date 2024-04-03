package toyproject.studyscheduler.study.application.dto;

import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

public interface StudyDtoSpec {

    Study toEntity();
    String getTitle();
    String getDescription();
    boolean isTermination();
    LocalDate getStartDate();
    LocalDate getEndDate();
    int getPlanMinutesInWeekday();
    int getPlanMinutesInWeekend();
}
