package toyproject.studyscheduler.api.service.study;

import toyproject.studyscheduler.api.controller.request.SaveStudyRequestDto;
import toyproject.studyscheduler.api.controller.request.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.api.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.domain.study.StudyType;

public interface StudyService {

    boolean supports(StudyType studyType);

    void saveStudy(SaveStudyRequestDto saveStudyRequestDto);

    FindStudyResponseDto findStudyById(Long id);

    int calculatePeriod(StudyPlanTimeRequestDto dto);
}
