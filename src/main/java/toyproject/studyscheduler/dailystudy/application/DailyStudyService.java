package toyproject.studyscheduler.dailystudy.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.common.response.ResponseCode;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySelectCondition;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudyUpdate;
import toyproject.studyscheduler.dailystudy.domain.DailyStudies;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.dailystudy.exception.DailyStudyException;
import toyproject.studyscheduler.dailystudy.repository.DailyStudyRepository;
import toyproject.studyscheduler.dailystudy.web.dto.*;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class DailyStudyService {

    private final StudyService studyService;

    private final DailyStudyRepository dailyStudyRepository;

    /**
     * 일일 학습 생성
     */
    public DailyStudyCreation createDailyStudy(DailyStudySave dailyStudySave) {
        Study study = findStudyNotTerminated(dailyStudySave.getStudyId());
        DailyStudy dailyStudy = createDailyStudyAfterValidation(dailyStudySave, study);

        return createDailyStudyCreation(study, dailyStudy);
    }

    private Study findStudyNotTerminated(Long studyId) {
        Study study = studyService.findStudyEntityById(studyId);

        study.getStudyInformation().validateTermination();

        return study;
    }

    private DailyStudy createDailyStudyAfterValidation(DailyStudySave dailyStudySave, Study study) {
        study.validateStudyDateEarlierThanStartDate(dailyStudySave.getStudyDate());

        return dailyStudyRepository.save(dailyStudySave.toEntity(study));
    }

    private DailyStudyCreation createDailyStudyCreation(Study study, DailyStudy dailyStudy) {
        StudyRemaining studyRemaining = calculateStudyRemaining(dailyStudy.getStudyDate(), study);

        return DailyStudyCreation.from(dailyStudy, studyRemaining);
    }

    /**
     * 일일 학습 수정
     */
    public DailyStudyUpdateResult updateDailyStudy(Long dailyStudyId, DailyStudyUpdate dailyStudyUpdate) {
        DailyStudy dailyStudy = findDailyStudy(dailyStudyId);
        Study study = getStudyAfterValidateStudyDate(dailyStudyUpdate, dailyStudy);

        dailyStudyUpdate.update(dailyStudy);
        return createDailyStudyUpdateResult(dailyStudy, study);
    }

    private static Study getStudyAfterValidateStudyDate(DailyStudyUpdate dailyStudyUpdate, DailyStudy dailyStudy) {
        Study study = dailyStudy.getStudy();

        study.validateStudyDateEarlierThanStartDate(dailyStudyUpdate.getStudyDate());

        return study;
    }

    private DailyStudyUpdateResult createDailyStudyUpdateResult(DailyStudy dailyStudy, Study study) {
        StudyRemaining studyRemaining = calculateStudyRemaining(dailyStudy.getStudyDate(), study);

        return DailyStudyUpdateResult.from(dailyStudy, studyRemaining);
    }

    /**
     *  일일 학습 아이디별 조회
     */
    public DailyStudyDetailInfo findDetailDailyStudy(Long dailyStudyId) {
        DailyStudy dailyStudy = findDailyStudy(dailyStudyId);

        return DailyStudyDetailInfo.of(dailyStudy);
    }

    /**
     *  학습 별, 기간 별 일일 학습 조회
     */
    public List<DailyStudyBasicInfo> findAllDailyStudyByStudy(DailyStudySelectCondition conditions) {
        List<DailyStudy> dailyStudies = dailyStudyRepository.findDailyStudyByConditions(conditions.getStudyId(), conditions.getPeriod());

        return dailyStudies.stream()
                .map(DailyStudyBasicInfo::of)
                .toList();
    }

    /**
     * 저장된 일일 학습량을 계산해 남은 일수, 종료 여부 등을 조회
     */
    public StudyRemaining getStudyRemainingInfo(Long studyId, LocalDate now) {
        Study study = findStudyNotTerminated(studyId);

        // 일일 학습을 등록한 상태가 아니기 때문에 전날을 인자로 전달해줘야 정상적으로 계산됨
        return calculateStudyRemaining(now.minusDays(1), study);
    }

    private StudyRemaining calculateStudyRemaining(LocalDate baseDate, Study study) {
        int totalStudyMinutes = getTotalStudyMinutes(study);

        // 오늘 학습한 내용까지 포함해야 하기 때문에 기준일의 다음 날부터 계산
        LocalDate expectedEndDate = study.calculateExpectedDate(totalStudyMinutes, baseDate.plusDays(1));
        boolean isTermination = study.terminateIfSatisfiedStudyQuantity(totalStudyMinutes, baseDate);

        return StudyRemaining.from(study.getType(), expectedEndDate, study.calculateRemainingQuantity(totalStudyMinutes), isTermination);
    }

    private int getTotalStudyMinutes(Study study) {
        DailyStudies dailyStudies = new DailyStudies(dailyStudyRepository.findDailyStudyByConditions(study.getId(), null));

        return dailyStudies.calculateTotalStudyMinutes();
    }

    private DailyStudy findDailyStudy(Long dailyStudyId) {
        return dailyStudyRepository.findById(dailyStudyId)
            .orElseThrow(() -> new DailyStudyException("dailyStudyId :: " + dailyStudyId, ResponseCode.E40000));
    }
}
