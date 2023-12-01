package toyproject.studyscheduler.service.study.lecture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.controller.request.study.StudySave;
import toyproject.studyscheduler.controller.request.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.service.study.StudyService;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.StudyType;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.util.StudyUtil;

import static toyproject.studyscheduler.domain.study.StudyType.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class LectureService implements StudyService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyUtil studyUtil;

    @Override
    public boolean supports(StudyType studyType) {
        return LECTURE == studyType;
    }

    @Override
    public void save(StudySave studySave) {
        Member member = memberRepository.findById(studySave.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Lecture lecture = studySave.toLectureEntity(member);
        studyRepository.save(lecture);
    }

    @Override
    public FindStudyResponseDto studyBy(Long id) {
        Lecture lecture = (Lecture) studyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 학습이 존재하지 않습니다."));

        return FindStudyResponseDto.ofLecture(lecture);
    }

    @Override
    public int calculatePeriod(StudyPlanTimeRequestDto dto) {
        return studyUtil.setUpPeriodCalCulatorBy(dto.getPlanTimeInWeekDay(), dto.getPlanTimeInWeekend(), dto.getStartDate())
            .calculatePeriodBy(dto.getTotalRunTime());
    }
}
