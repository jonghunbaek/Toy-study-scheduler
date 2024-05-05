package toyproject.studyscheduler.dailystudy.repository;

import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.study.application.dto.Period;

import java.util.List;

public interface DailyStudyQuerydslRepository {

    List<DailyStudy> findDailyStudyByStudyAndPeriod(Long studyId, Period period);
}
