package toyproject.studyscheduler.auth.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.member.entity.Role;

@NoArgsConstructor
@Getter
public class MemberInfo {

    private long memberId;
    private Role role;

    private MemberInfo(long memberId, Role role) {
        this.memberId = memberId;
        this.role = role;
    }

    public static MemberInfo of(Member member) {
        return new MemberInfo(member.getId(), member.getRole());
    }
}
