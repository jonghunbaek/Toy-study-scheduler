package toyproject.studyscheduler.dailystudy.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.common.domain.Period;
import toyproject.studyscheduler.common.validator.ValidatorGroupSequence;
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

    // TODO :: 관련 테스트 수정 필요
    @GetMapping("/study")
    public List<DailyStudyBasicInfo> dailyStudyBasicInfos(
            @RequestParam @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long studyId,
            @Validated(ValidatorGroupSequence.class) Period period) {
        return dailyStudyService.findAllDailyStudyByStudy(studyId, period);
    }

    @GetMapping("/remaining")
    public StudyRemaining getExpectedEndDate(@RequestParam @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long studyId) {
        return dailyStudyService.getStudyRemainingInfo(studyId, LocalDate.now());
    }
}
