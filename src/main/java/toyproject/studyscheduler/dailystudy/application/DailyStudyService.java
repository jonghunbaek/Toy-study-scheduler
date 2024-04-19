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

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class DailyStudyService {

    private final StudyService studyService;

    private final DailyStudyRepository dailyStudyRepository;

    // TODO :: 일일 학습 생성 시 일일 학습일이 학습 시작일 이전인지 검증해야 함.
    public DailyStudyCreation createDailyStudy(DailyStudySave dailyStudySave) {
        Study study = studyService.findById(dailyStudySave.getStudyId());
        study.getStudyInformation().validateTermination();

        DailyStudy dailyStudy = dailyStudyRepository.save(dailyStudySave.toEntity(study));

        return DailyStudyCreation.of(dailyStudy, isStudyTerminated(study));
    }

    private boolean isStudyTerminated(Study study) {
        DailyStudies dailyStudies = new DailyStudies(dailyStudyRepository.findAllByStudy(study));

        int totalMinutes = dailyStudies.calculateTotalStudyMinutes();

        return study.terminateIfSatisfiedStudyQuantity(totalMinutes);
    }
}
