package toyproject.studyscheduler.study.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.member.application.MemberService;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.application.dto.Period;
import toyproject.studyscheduler.study.application.dto.StudySave;
import toyproject.studyscheduler.study.domain.entity.Study;
import toyproject.studyscheduler.study.repository.StudyRepository;
import toyproject.studyscheduler.study.web.dto.StudyCreation;
import toyproject.studyscheduler.study.web.dto.StudyInAction;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final MemberService memberService;

    private final StudyRepository studyRepository;

    public StudyCreation createStudy(StudySave studySaveDto, Long memberId) {
        Member member = memberService.findMemberBy(memberId);
        Study savedStudy = studyRepository.save(studySaveDto.toStudy(member));

        return StudyCreation.of(savedStudy);
    }

    public List<StudyInAction> findStudiesByPeriod(Period period, long memberId) {
        List<Study> studies = studyRepository.findAllByPeriod(period.getStartDate(), period.getEndDate(), memberId);

        return studies.stream()
            .map(StudyInAction::of)
            .toList();
    }
}
