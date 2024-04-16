package toyproject.studyscheduler.dailystudy.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.dailystudy.application.dto.DailyStudySave;
import toyproject.studyscheduler.dailystudy.domain.entity.DailyStudy;
import toyproject.studyscheduler.dailystudy.repository.DailyStudyRepository;
import toyproject.studyscheduler.dailystudy.web.dailystudy.DailyStudyCreation;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.study.domain.entity.Study;

@RequiredArgsConstructor
@Transactional
@Service
public class DailyStudyService {

    private final StudyService studyService;

    private final DailyStudyRepository dailyStudyRepository;

    // TODO :: 일일 학습 생성 시 잔여 학습량 확인 후 학습 종료 여부를 확인해야 함.
    public DailyStudyCreation createDailyStudy(DailyStudySave dailyStudySave) {
        Study study = studyService.findById(dailyStudySave.getStudyId());

        DailyStudy dailyStudy = dailyStudyRepository.save(dailyStudySave.toEntity(study));

        return DailyStudyCreation.of(dailyStudy);
    }
}
