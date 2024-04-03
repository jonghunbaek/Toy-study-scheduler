package toyproject.studyscheduler.study.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.study.application.dto.LectureSaveDto;
import toyproject.studyscheduler.study.domain.entity.Study;
import toyproject.studyscheduler.study.repository.StudyRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;

    // TODO :: 학습 저장시 다형성 활용할 수 있는지 시도해보기
    public Study createStudy(LectureSaveDto lectureSaveDto) {
        Study entity = lectureSaveDto.toEntity();
        studyRepository.save(entity);
        return null;
    }

}
