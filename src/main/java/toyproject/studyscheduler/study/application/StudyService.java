package toyproject.studyscheduler.study.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.member.application.MemberService;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.study.application.dto.Period;
import toyproject.studyscheduler.study.application.dto.StudySave;
import toyproject.studyscheduler.study.application.dto.StudyUpdate;
import toyproject.studyscheduler.study.domain.entity.Study;
import toyproject.studyscheduler.study.exception.StudyException;
import toyproject.studyscheduler.study.repository.StudyRepository;
import toyproject.studyscheduler.study.web.dto.StudyDetail;
import toyproject.studyscheduler.study.web.dto.StudyInAction;

import java.util.List;

import static toyproject.studyscheduler.common.response.ResponseCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final MemberService memberService;

    private final StudyRepository studyRepository;

    public StudyDetail createStudy(StudySave studySaveDto, Long memberId) {
        Member member = memberService.findMemberBy(memberId);
        Study savedStudy = studyRepository.save(studySaveDto.toStudy(member));

        return StudyDetail.of(savedStudy);
    }

    public StudyDetail findStudyById(Long studyId) {
        Study study = findById(studyId);

        return StudyDetail.of(study);
    }

    public List<StudyInAction> findStudiesByPeriod(Period period, long memberId) {
        List<Study> studies = studyRepository.findAllByPeriod(period.getStartDate(), period.getEndDate(), memberId);

        return studies.stream()
            .map(StudyInAction::of)
            .toList();
    }

    public void updateStudy(StudyUpdate studyUpdate) {
        Study study = findById(studyUpdate.getStudyId());

    }

    private Study findById(Long studyId) {
        return studyRepository.findById(studyId)
            .orElseThrow(() -> new StudyException("studyId :: " + studyId, E30002));
    }
}
