package toyproject.studyscheduler.study.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.study.application.dto.StudySaveSpec;
import toyproject.studyscheduler.study.web.dto.StudyCreationSpec;

@RequestMapping("/studies")
@RequiredArgsConstructor
@RestController
public class StudyController {

    private final StudyService studyService;

    /**
     * 새로운 학습을 저장한다.
     * 종료되지 않은 스터디는 예상 종료일을 계산해 같이 반환한다.
     */
    @PostMapping
    public StudyCreationSpec createStudy(@RequestBody @Valid StudySaveSpec studySaveSpec) {
        return studyService.createStudy(studySaveSpec);
    }
}
