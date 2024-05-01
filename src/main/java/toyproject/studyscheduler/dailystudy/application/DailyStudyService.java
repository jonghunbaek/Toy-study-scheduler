package toyproject.studyscheduler.dailystudy.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.common.response.ResponseCode;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudyUpdate;
import toyproject.studyscheduler.dailystudy.domain.DailyStudies;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.dailystudy.exception.DailyStudyException;
import toyproject.studyscheduler.dailystudy.repository.DailyStudyRepository;
import toyproject.studyscheduler.dailystudy.web.dto.DailyStudyCreation;
import toyproject.studyscheduler.dailystudy.web.dto.DailyStudyUpdateResult;
import toyproject.studyscheduler.dailystudy.web.dto.RemainingStudyDays;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
@Transactional
@Service
public class DailyStudyService {

    private final StudyService studyService;

    private final DailyStudyRepository dailyStudyRepository;

    public DailyStudyCreation createDailyStudy(DailyStudySave dailyStudySave) {
        Study study = findStudyNotTerminated(dailyStudySave.getStudyId());
        DailyStudy dailyStudy = createDailyStudyAfterValidation(dailyStudySave, study);

        return createDailyStudyCreation(study, dailyStudy);
    }

    private DailyStudy createDailyStudyAfterValidation(DailyStudySave dailyStudySave, Study study) {
        study.validateStudyDateEarlierThanStartDate(dailyStudySave.getStudyDate());

        return dailyStudyRepository.save(dailyStudySave.toEntity(study));
    }

    private DailyStudyCreation createDailyStudyCreation(Study study, DailyStudy dailyStudy) {
        int totalStudyMinutes = getTotalStudyMinutes(study);
        LocalDate studyDate = dailyStudy.getStudyDate();
        LocalDate nextStudyDate = studyDate.plusDays(1); // 다음 날 부터 예상 종료일을 계산해야함

        LocalDate expectedEndDate = study.calculateExpectedDate(totalStudyMinutes, nextStudyDate);
        boolean isTerminated = study.terminateIfSatisfiedStudyQuantity(totalStudyMinutes, studyDate);

        return DailyStudyCreation.from(dailyStudy, isTerminated, expectedEndDate, study.calculateRemainingQuantity(totalStudyMinutes));
    }

    public DailyStudyUpdateResult updateDailyStudy(Long dailyStudyId, DailyStudyUpdate dailyStudyUpdate) {
        DailyStudy dailyStudy = findDailyStudy(dailyStudyId);
        Study study = dailyStudy.getStudy();

        validateStudyDateAndTermination(dailyStudyUpdate, study);

        dailyStudyUpdate.update(dailyStudy);
        return createDailyStudyUpdateResult(dailyStudy, study);
    }

    private DailyStudyUpdateResult createDailyStudyUpdateResult(DailyStudy dailyStudy, Study study) {
        int totalStudyMinutes = getTotalStudyMinutes(study);
        LocalDate studyDate = dailyStudy.getStudyDate();
        LocalDate nextStudyDate = studyDate.plusDays(1);

        LocalDate expectedDate = study.calculateExpectedDate(totalStudyMinutes, nextStudyDate);
        boolean isTermination = study.terminateIfSatisfiedStudyQuantity(totalStudyMinutes, studyDate);

        return new DailyStudyUpdateResult(study.calculateRemainingQuantity(totalStudyMinutes), expectedDate, isTermination);
    }

    private void validateStudyDateAndTermination(DailyStudyUpdate dailyStudyUpdate, Study study) {
        study.validateStudyDateEarlierThanStartDate(dailyStudyUpdate.getStudyDate());
        study.getStudyInformation().validateTermination();
    }

    private DailyStudy findDailyStudy(Long dailyStudyId) {
        return dailyStudyRepository.findById(dailyStudyId)
            .orElseThrow(() -> new DailyStudyException("dailyStudyId :: " + dailyStudyId, ResponseCode.E40000));
    }

    public RemainingStudyDays calculateExpectedEndDate(Long studyId, LocalDate now) {
        Study study = findStudyNotTerminated(studyId);
        int totalStudyMinutes = getTotalStudyMinutes(study);
        LocalDate expectedDate = study.calculateExpectedDate(totalStudyMinutes, now);

        return new RemainingStudyDays(expectedDate, Period.between(now, expectedDate).getDays());
    }

    private Study findStudyNotTerminated(Long studyId) {
        Study study = studyService.findById(studyId);

        study.getStudyInformation().validateTermination();

        return study;
    }

    private int getTotalStudyMinutes(Study study) {
        DailyStudies dailyStudies = new DailyStudies(dailyStudyRepository.findAllByStudy(study));

        return dailyStudies.calculateTotalStudyMinutes();
    }
}
