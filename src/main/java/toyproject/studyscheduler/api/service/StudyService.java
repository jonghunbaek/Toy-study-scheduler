package toyproject.studyscheduler.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class StudyService {

    private final StudyRepository studyRepository;
}
