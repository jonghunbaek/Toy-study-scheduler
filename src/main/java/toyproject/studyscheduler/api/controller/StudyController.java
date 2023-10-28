package toyproject.studyscheduler.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.api.controller.request.SaveStudyRequestDto;
import toyproject.studyscheduler.api.controller.request.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.api.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.api.service.study.StudyFactory;
import toyproject.studyscheduler.api.service.study.StudyService;
import toyproject.studyscheduler.domain.study.StudyType;

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

    @ResponseBody
    @GetMapping("/studies/{id}")
    public FindStudyResponseDto getStudyBy(@PathVariable Long id, @RequestHeader StudyType studyType) {
        StudyService studyService = studyFactory.findServiceBy(studyType);
        return studyService.findStudyById(id);
    }

    @ResponseBody
    @GetMapping("/studies/period")
    public int getPeriod(@ModelAttribute StudyPlanTimeRequestDto dto) {
        StudyService studyService = studyFactory.findServiceBy(dto.getStudyType());
        return studyService.calculatePeriod(dto);
    }
}
