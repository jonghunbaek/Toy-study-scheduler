package toyproject.studyscheduler.study.application;

import toyproject.studyscheduler.study.web.dto.StudySave;
import toyproject.studyscheduler.study.web.dto.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.study.web.dto.FindStudyResponseDto;
import toyproject.studyscheduler.study.entity.domain.StudyType;

public interface StudyService {

    boolean supports(StudyType studyType);

    void save(StudySave studySave);

    FindStudyResponseDto studyBy(Long id);

    int calculatePeriod(StudyPlanTimeRequestDto dto);
}
