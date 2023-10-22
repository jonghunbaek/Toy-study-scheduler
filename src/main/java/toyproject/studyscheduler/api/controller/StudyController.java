package toyproject.studyscheduler.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import toyproject.studyscheduler.api.controller.request.SaveStudyRequestDto;
import toyproject.studyscheduler.api.service.study.StudyFactory;
import toyproject.studyscheduler.api.service.study.StudyService;

@RequiredArgsConstructor
@Controller
public class StudyController {

    private final StudyFactory studyFactory;

    @PostMapping("/studies")
    public void saveStudy(SaveStudyRequestDto saveStudyRequestDto) {
        StudyService studyService = studyFactory.getSuitableService(saveStudyRequestDto.getStudyType());
        studyService.saveStudy(saveStudyRequestDto);
    }
}
