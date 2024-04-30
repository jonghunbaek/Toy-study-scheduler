package toyproject.studyscheduler.dailystudy.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;
import toyproject.studyscheduler.dailystudy.domain.DailyStudies;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.dailystudy.repository.DailyStudyRepository;
import toyproject.studyscheduler.dailystudy.web.dto.DailyStudyCreation;
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

        return DailyStudyCreation.from(dailyStudy, isTerminated, expectedEndDate, study.getTotalMinutes() - totalStudyMinutes);
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
