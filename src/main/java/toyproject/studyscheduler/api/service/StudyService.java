package toyproject.studyscheduler.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.api.controller.request.SaveStudyRequestDto;
import toyproject.studyscheduler.api.controller.request.StudyPlanTimeRequestDto;
import toyproject.studyscheduler.api.controller.response.FindStudyResponseDto;
import toyproject.studyscheduler.api.controller.response.FindStudyTimeResponseDto;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.StudyTime;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.domain.study.repository.StudyTimeRepository;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.util.PeriodCalculator;
import toyproject.studyscheduler.util.StudyUtil;

import java.time.LocalDate;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyTimeRepository studyTimeRepository;
    private final MemberRepository memberRepository;
    private final StudyUtil studyUtil;

    public void saveStudy(SaveStudyRequestDto dto) {
        String studyType = dto.getStudyType();
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));

        Study study;
        if ("lecture".equals(studyType)) {
            study = dto.toLectureEntity(member);
        } else if ("reading".equals(studyType)) {
            study = dto.toReadingEntity(member);
        } else {
            study = dto.toToyProjectEntity(member);
        }

        studyRepository.save(study);
    }

    /**
     * id로 Study 상세 내용 조회
     * @param id
     * @return
     */
    public FindStudyResponseDto findStudyById(Long id) {
        Study study = studyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 학습 아이디는 존재하지 않습니다."));

        if ("lecture".equals(study.getStudyType())) {
            return FindStudyResponseDto.ofLecture((Lecture) study);
        } else if ("reading".equals(study.getStudyType())) {
            return FindStudyResponseDto.ofReading((Reading) study);
        } else {
            return FindStudyResponseDto.ofToyProject((ToyProject) study);
        }
    }

    /**
     * 특정 기간 동안 학습한 학습시간 모두 조회
     * @param startDate
     * @param endDate
     * @return
     */
    public List<FindStudyTimeResponseDto> findAllBy(LocalDate startDate, LocalDate endDate) {
        List<StudyTime> studyTimes = studyTimeRepository.findAllByPeriod(startDate, endDate);

        return studyTimes.stream()
            .map(studyTime -> FindStudyTimeResponseDto.of(
                studyTime.getStudy().getId(),
                studyTime.getStudy().getTitle(),
                studyTime.getStudy().getDescription(),
                studyTime.getStudy().isTermination(),
                studyTime.getTotalCompleteTime(),
                studyTime.getTotalLearningRate(),
                studyTime.getCompleteTimeToday(),
                studyTime.getDate()
            ))
            .toList();
    }

    public int calculatePeriod(StudyPlanTimeRequestDto dto) {
        PeriodCalculator periodCalculator = studyUtil.setUpPeriodCalCulatorBy(dto.getPlanTimeInWeekDay(), dto.getPlanTimeInWeekend(), dto.getStartDate());
        if ("lecture".equals(dto.getStudyType())) {
            return periodCalculator.calculatePeriodBy(dto.getTotalRunTime());
        } else if ("reading".equals(dto.getStudyType())) {
            return periodCalculator.calculatePeriodBy(dto.getTotalPage(), dto.getReadPagePerMin());
        } else {
            return periodCalculator.calculatePeriodBy(dto.getExpectedTimes());
        }
    }
}
