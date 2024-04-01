package toyproject.studyscheduler.study.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.study.web.dto.StudySave;
import toyproject.studyscheduler.study.web.dto.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.study.web.dto.FindStudyResponseDto;
import toyproject.studyscheduler.study.application.StudyFactory;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.study.domain.StudyType;

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
