package toyproject.studyscheduler.dailystudy.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;
import toyproject.studyscheduler.dailystudy.domain.DailyStudies;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.dailystudy.repository.DailyStudyRepository;
import toyproject.studyscheduler.dailystudy.web.dailystudy.DailyStudyCreation;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.study.domain.entity.Study;

import java.time.LocalDate;

@RequiredArgsConstructor
@Transactional
@Service
public class DailyStudyService {

    private final StudyService studyService;

    private final DailyStudyRepository dailyStudyRepository;

    // TODO :: 일일 학습 등록 후 남은 학습일을 계산해서 반환해줘야함
    public DailyStudyCreation createDailyStudy(DailyStudySave dailyStudySave) {
        Study study = getStudyAndValidateTermination(dailyStudySave.getStudyId());
        DailyStudy dailyStudy = createDailyStudyAfterValidation(dailyStudySave, study);

        return DailyStudyCreation.of(dailyStudy, isStudyTerminated(study, dailyStudy.getStudyDate()));
    }

    private Study getStudyAndValidateTermination(Long studyId) {
        Study study = studyService.findById(studyId);

        study.getStudyInformation().validateTermination();

        return study;
    }

    private DailyStudy createDailyStudyAfterValidation(DailyStudySave dailyStudySave, Study study) {
        study.validateStudyDateEarlierThanStartDate(dailyStudySave.getStudyDate());

        return dailyStudyRepository.save(dailyStudySave.toEntity(study));
    }

    private boolean isStudyTerminated(Study study, LocalDate studyDate) {
        DailyStudies dailyStudies = new DailyStudies(dailyStudyRepository.findAllByStudy(study));

        int totalMinutes = dailyStudies.calculateTotalStudyMinutes();

        return study.terminateIfSatisfiedStudyQuantity(totalMinutes, studyDate);
    }
}
