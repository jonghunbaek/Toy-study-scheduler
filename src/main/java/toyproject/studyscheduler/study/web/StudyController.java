package toyproject.studyscheduler.study.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.studyscheduler.common.resolver.LoginMember;
import toyproject.studyscheduler.common.resolver.MemberInfo;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.study.application.dto.Period;
import toyproject.studyscheduler.study.application.dto.StudySave;
import toyproject.studyscheduler.study.web.dto.StudyCreation;
import toyproject.studyscheduler.study.web.dto.StudyInAction;

import java.util.List;

@RequestMapping("/studies")
@RequiredArgsConstructor
//@Validated
@RestController
public class StudyController {

    private final StudyService studyService;

    /**
     * 새로운 학습을 저장한다.
     * 종료되지 않은 스터디는 예상 종료일을 계산해 같이 반환한다.
     */
    @PostMapping
    public StudyCreation studySave(@RequestBody @Valid StudySave studySave, @LoginMember MemberInfo memberInfo) {
        return studyService.createStudy(studySave, memberInfo.getMemberId());
    }

    @GetMapping("/period")
    public List<StudyInAction> studiesInActionDuringPeriod(@Valid Period period, @LoginMember MemberInfo memberInfo) {
        return studyService.findStudiesByPeriod(period, memberInfo.getMemberId());
    }
}
