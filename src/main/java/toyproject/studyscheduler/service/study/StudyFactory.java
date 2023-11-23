package toyproject.studyscheduler.service.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toyproject.studyscheduler.domain.study.StudyType;

import java.util.List;

@RequiredArgsConstructor
@Component
public class StudyFactory {

    private final List<StudyService> studyServices;

    public StudyService findServiceBy(StudyType studyType) {
        return studyServices.stream()
            .filter(studyService -> studyService.supports(studyType))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("적합한 스터디 종류를 찾지 못했습니다."));
    }
}
