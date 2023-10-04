package toyproject.studyscheduler.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.api.request.SaveStudyRequestDto;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.lecture.Lecture;
import toyproject.studyscheduler.domain.study.reading.Reading;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.domain.study.repository.StudyTimeRepository;
import toyproject.studyscheduler.domain.study.toyproject.ToyProject;
import toyproject.studyscheduler.util.StudyUtil;

import java.time.LocalDate;

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

        Study study = null;
        if ("lecture".equals(studyType)) {
            study = Lecture.builder()
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .totalExpectedPeriod(dto.getTotalExpectedPeriod())
                    .planTimeInWeekday(dto.getPlanTimeInWeekday())
                    .planTimeInWeekend(dto.getPlanTimeInWeekend())
                    .startDate(dto.getStartDate())
                    .expectedEndDate(dto.getStartDate().plusDays(dto.getTotalExpectedPeriod()))
                    .member(member)
                    .teacherName(dto.getTeacherName())
                    .totalRuntime(dto.getTotalRuntime())
                    .build();
        }

        if ("reading".equals(studyType)) {
            study = Reading.builder()
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .totalExpectedPeriod(dto.getTotalExpectedPeriod())
                    .planTimeInWeekday(dto.getPlanTimeInWeekday())
                    .planTimeInWeekend(dto.getPlanTimeInWeekend())
                    .startDate(dto.getStartDate())
                    .expectedEndDate(dto.getStartDate().plusDays(dto.getTotalExpectedPeriod()))
                    .totalPage(dto.getTotalPage())
                    .authorName(dto.getAuthorName())
                    .readPagePerMin(dto.getReadPagePerMin())
                    .member(member)
                    .build();
        }

        if ("toy".equals(studyType)) {
            study = ToyProject.builder()
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .totalExpectedPeriod(dto.getTotalExpectedPeriod())
                    .planTimeInWeekday(dto.getPlanTimeInWeekday())
                    .planTimeInWeekend(dto.getPlanTimeInWeekend())
                    .startDate(dto.getStartDate())
                    .expectedEndDate(dto.getStartDate().plusDays(dto.getTotalExpectedPeriod()))
                    .build();
        }

        studyRepository.save(study);
    }
}
