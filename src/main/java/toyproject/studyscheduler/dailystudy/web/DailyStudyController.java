package toyproject.studyscheduler.dailystudy.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.dailystudy.application.DailyStudyService;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudyUpdate;
import toyproject.studyscheduler.dailystudy.web.dto.*;

import java.time.LocalDate;
import java.util.List;

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

    @PutMapping("/{dailyStudyId}")
    public DailyStudyUpdateResult dailyStudyUpdate(
        @PathVariable @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long dailyStudyId,
        @RequestBody @Valid DailyStudyUpdate dailyStudyUpdate) {
        return dailyStudyService.updateDailyStudy(dailyStudyId, dailyStudyUpdate);
    }

    @GetMapping
    public DailyStudyDetailInfo dailyStudyDetailInfo(@RequestParam @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long dailyStudyId) {
        return dailyStudyService.findDetailDailyStudy(dailyStudyId);
    }

    @GetMapping("/{studyId}")
    public List<DailyStudyBasicInfo> dailyStudyBasicInfos(@PathVariable @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long studyId) {
        return dailyStudyService.findAllDailyStudyByStudy(studyId);
    }

    @GetMapping("/remaining-days")
    public StudyRemaining getExpectedEndDate(@RequestParam @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long studyId) {
        return dailyStudyService.getStudyRemainingInfo(studyId, LocalDate.now());
    }
}
