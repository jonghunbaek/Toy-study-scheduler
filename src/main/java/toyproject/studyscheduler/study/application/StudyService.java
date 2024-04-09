package toyproject.studyscheduler.study.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.member.application.MemberService;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.application.dto.StudySave;
import toyproject.studyscheduler.study.domain.entity.Study;
import toyproject.studyscheduler.study.repository.StudyRepository;
import toyproject.studyscheduler.study.web.dto.StudyCreation;

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
}
