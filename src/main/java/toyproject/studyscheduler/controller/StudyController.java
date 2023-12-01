package toyproject.studyscheduler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.controller.request.study.StudySave;
import toyproject.studyscheduler.controller.request.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.service.study.StudyFactory;
import toyproject.studyscheduler.service.study.StudyService;
import toyproject.studyscheduler.domain.study.StudyType;

@RequiredArgsConstructor
@RestController
public class StudyController {

    private final StudyFactory studyFactory;

    @PostMapping("/studies")
    public void saveStudy(StudySave studySave) {
        StudyService studyService = studyFactory.serviceBy(studySave.getStudyType());
        studyService.save(studySave);
    }

    @GetMapping("/studies/{id}")
    public FindStudyResponseDto getStudyBy(@PathVariable Long id, @RequestParam StudyType studyType) {
        StudyService studyService = studyFactory.serviceBy(studyType);
        return studyService.studyBy(id);
    }

    @GetMapping("/studies/period")
    public int getPeriod(@ModelAttribute StudyPlanTimeRequestDto dto) {
        StudyService studyService = studyFactory.serviceBy(dto.getStudyType());
        return studyService.calculatePeriod(dto);
    }
}
