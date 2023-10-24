package toyproject.studyscheduler.api.service.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.studyscheduler.api.controller.request.SaveStudyRequestDto;
import toyproject.studyscheduler.api.controller.request.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.api.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.StudyType;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.util.StudyUtil;

import static toyproject.studyscheduler.domain.study.StudyType.*;

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
    public void saveStudy(SaveStudyRequestDto saveStudyRequestDto) {
        Member member = memberRepository.findById(saveStudyRequestDto.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Reading reading = saveStudyRequestDto.toReadingEntity(member);
        studyRepository.save(reading);
    }

    @Override
    public FindStudyResponseDto findStudyById(Long id) {
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
