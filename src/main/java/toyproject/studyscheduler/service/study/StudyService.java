package toyproject.studyscheduler.service.study;

import toyproject.studyscheduler.controller.request.study.SaveStudyRequestDto;
import toyproject.studyscheduler.controller.request.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.domain.study.StudyType;

public interface StudyService {

    boolean supports(StudyType studyType);

    void saveStudy(SaveStudyRequestDto saveStudyRequestDto);

    FindStudyResponseDto findStudyById(Long id);

    int calculatePeriod(StudyPlanTimeRequestDto dto);
}
