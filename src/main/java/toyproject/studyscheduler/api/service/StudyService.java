package toyproject.studyscheduler.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.api.request.SaveStudyRequestDto;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
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

    public void saveStudy(SaveStudyRequestDto dto) {
        String studyType = dto.getStudyType();

        if ("lecture".equals(studyType)) {
            Lecture.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
        }

    }
}
