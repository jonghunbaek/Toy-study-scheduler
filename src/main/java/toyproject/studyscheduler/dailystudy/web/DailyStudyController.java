package toyproject.studyscheduler.dailystudy.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.dailystudy.application.DailyStudyService;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;
import toyproject.studyscheduler.dailystudy.web.dto.DailyStudyCreation;
import toyproject.studyscheduler.dailystudy.web.dto.StudyRemaining;

import java.time.LocalDate;

@Validated
@RequestMapping("/daily-studies")
@RequiredArgsConstructor
@RestController
public class DailyStudyController {

    private final DailyStudyService dailyStudyService;

    @PostMapping
    public DailyStudyCreation dailyStudyCreate(@RequestBody @Valid DailyStudySave dailyStudySave) {
        return dailyStudyService.createDailyStudy(dailyStudySave);
    }

    @GetMapping("/remaining-days")
    public StudyRemaining getExpectedEndDate(@RequestParam @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long studyId) {
        return dailyStudyService.getStudyRemainingInfo(studyId, LocalDate.now());
    }
}
