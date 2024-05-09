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

    /**
     *  새로운 일일 학습 생성 및 저장
     */
    @PostMapping
    public DailyStudyCreation dailyStudyCreate(@RequestBody @Valid DailyStudySave dailyStudySave) {
        return dailyStudyService.createDailyStudy(dailyStudySave);
    }

    /**
     *  일일 학습 수정
     */
    @PutMapping("/{dailyStudyId}")
    public DailyStudyUpdateResult dailyStudyUpdate(
        @PathVariable @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long dailyStudyId,
        @RequestBody @Valid DailyStudyUpdate dailyStudyUpdate) {
        return dailyStudyService.updateDailyStudy(dailyStudyId, dailyStudyUpdate);
    }

    /**
     *  일일 학습 아이디로 일일 학습 상세 정보 조회
     */
    @GetMapping
    public DailyStudyDetailInfo dailyStudyDetailInfo(@RequestParam @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long dailyStudyId) {
        return dailyStudyService.findDetailDailyStudy(dailyStudyId);
    }

    /**
     *  학습 아이디, 기간 조건에 해당하는 모든 일일 학습 조회
     */
    @GetMapping("/study")
    public List<DailyStudyBasicInfo> dailyStudyBasicInfos(
            @RequestParam @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long studyId,
            @Validated(ValidatorGroupSequence.class) Period period) {
        return dailyStudyService.findAllDailyStudyByStudy(studyId, period);
    }

    /**
     *  등록된 학습, 일일 학습들을 기준으로 남은 학습기간 등을 계산
     */
    @GetMapping("/remaining")
    public StudyRemaining getExpectedEndDate(@RequestParam @Min(value = 1, message = "id 값은 양의 정수이어야 합니다.") Long studyId) {
        return dailyStudyService.getStudyRemainingInfo(studyId, LocalDate.now());
    }
}
