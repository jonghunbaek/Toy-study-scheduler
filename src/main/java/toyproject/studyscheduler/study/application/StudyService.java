package toyproject.studyscheduler.study.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.study.application.dto.StudySave;
import toyproject.studyscheduler.study.domain.entity.Study;
import toyproject.studyscheduler.study.repository.StudyRepository;
import toyproject.studyscheduler.study.web.dto.StudyCreation;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;

    public StudyCreation createStudy(StudySave studySaveDto) {
        Study savedStudy = studyRepository.save(studySaveDto.toStudy());

        return StudyCreation.of(savedStudy);
    }
}
