package toyproject.studyscheduler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.controller.request.study.SaveStudyRequestDto;
import toyproject.studyscheduler.controller.request.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.service.study.StudyFactory;
import toyproject.studyscheduler.service.study.StudyService;
import toyproject.studyscheduler.domain.study.StudyType;

@RequiredArgsConstructor
@RestController
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
