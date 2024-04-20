package toyproject.studyscheduler.dailystudy.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.dailystudy.application.DailyStudyService;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;
import toyproject.studyscheduler.dailystudy.web.dto.DailyStudyCreation;
import toyproject.studyscheduler.dailystudy.web.dto.RemainingStudyDays;

import java.time.LocalDate;

@RequestMapping("/daily-studies")
@RequiredArgsConstructor
@RestController
public class DailyStudyController {

    private final DailyStudyService dailyStudyService;

    @PostMapping
    public DailyStudyCreation createDailyStudy(DailyStudySave dailyStudySave) {
        return dailyStudyService.createDailyStudy(dailyStudySave);
    }

    @GetMapping("/remaining-days")
    public RemainingStudyDays getExpectedEndDate(@RequestParam Long studyId) {
        return dailyStudyService.calculateExpectedEndDate(studyId, LocalDate.now());
    }
}
