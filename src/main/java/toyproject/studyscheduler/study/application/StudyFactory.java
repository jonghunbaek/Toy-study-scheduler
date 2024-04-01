package toyproject.studyscheduler.study.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toyproject.studyscheduler.study.domain.StudyType;

import java.util.List;

@RequiredArgsConstructor
@Component
public class StudyFactory {

    private final List<StudyService> studyServices;

    public StudyService serviceBy(StudyType studyType) {
        return studyServices.stream()
            .filter(studyService -> studyService.supports(studyType))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("적합한 스터디 종류를 찾지 못했습니다."));
    }
}
