package toyproject.studyscheduler.api.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.studyscheduler.domain.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
}
