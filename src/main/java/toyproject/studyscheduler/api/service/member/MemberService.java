package toyproject.studyscheduler.api.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.studyscheduler.api.controller.request.member.SaveMemberRequestDto;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void saveMember(SaveMemberRequestDto requestDto) {
        memberRepository.save(requestDto.toEntity());
    }
}
