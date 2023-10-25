package toyproject.studyscheduler.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import toyproject.studyscheduler.api.controller.request.SaveStudyRequestDto;
import toyproject.studyscheduler.api.service.study.StudyFactory;
import toyproject.studyscheduler.api.service.study.StudyService;

@RequiredArgsConstructor
@Controller
public class StudyController {

    private final StudyFactory studyFactory;

    @GetMapping("/studies/form")
    public String getSavingStudyForm() {
        return "save-study";
    }

    @PostMapping("/studies")
    public void saveStudy(SaveStudyRequestDto saveStudyRequestDto) {
        StudyService studyService = studyFactory.findServiceBy(saveStudyRequestDto.getStudyType());
        studyService.saveStudy(saveStudyRequestDto);
    }
}
