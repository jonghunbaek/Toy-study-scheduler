package toyproject.studyscheduler.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.api.request.SaveStudyRequestDto;
import toyproject.studyscheduler.domain.member.Member;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;
import toyproject.studyscheduler.domain.study.Study;
import toyproject.studyscheduler.domain.study.repository.StudyRepository;
import toyproject.studyscheduler.domain.study.repository.StudyTimeRepository;
import toyproject.studyscheduler.util.StudyUtil;

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

    public Study findStudyById(Long id) {
        return studyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 학습 아이디는 존재하지 않습니다."));
    }
}
