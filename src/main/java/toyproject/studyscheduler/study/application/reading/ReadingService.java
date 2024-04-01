package toyproject.studyscheduler.study.application.reading;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.study.web.dto.StudySave;
import toyproject.studyscheduler.study.web.dto.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.study.web.dto.FindStudyResponseDto;
import toyproject.studyscheduler.study.application.StudyService;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.member.repository.MemberRepository;
import toyproject.studyscheduler.study.domain.StudyType;
import toyproject.studyscheduler.study.domain.entity.Reading;
import toyproject.studyscheduler.study.repository.StudyRepository;
import toyproject.studyscheduler.common.util.StudyUtil;

import static toyproject.studyscheduler.study.domain.StudyType.*;

@Transactional
@RequiredArgsConstructor
@Service
public class ReadingService implements StudyService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyUtil studyUtil;

    @Override
    public boolean supports(StudyType studyType) {
        return READING == studyType;
    }

    @Override
    public void save(StudySave studySave) {
        Member member = memberRepository.findById(studySave.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Reading reading = studySave.toReadingEntity(member);
        studyRepository.save(reading);
    }

    @Override
    public FindStudyResponseDto studyBy(Long id) {
        Reading reading = (Reading) studyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 학습이 존재하지 않습니다."));

        return FindStudyResponseDto.ofReading(reading);
    }

    @Override
    public int calculatePeriod(StudyPlanTimeRequestDto dto) {
        return studyUtil.setUpPeriodCalCulatorBy(dto.getPlanTimeInWeekDay(), dto.getPlanTimeInWeekend(), dto.getStartDate())
            .calculatePeriodBy(dto.getTotalPage(), dto.getReadPagePerMin());
    }
}
