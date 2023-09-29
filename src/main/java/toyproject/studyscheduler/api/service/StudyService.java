package toyproject.studyscheduler.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.domain.study.repository.StudyTimeRepository;
import toyproject.studyscheduler.util.StudyUtil;

import java.time.LocalDate;

@Transactional
@RequiredArgsConstructor
@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyTimeRepository studyTimeRepository;
    private final StudyUtil studyUtil;

}
