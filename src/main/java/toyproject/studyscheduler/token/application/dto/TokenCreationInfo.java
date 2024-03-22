package toyproject.studyscheduler.token.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.member.entity.Member;
import toyproject.studyscheduler.member.entity.Role;

@NoArgsConstructor
@Getter
public class TokenCreationInfo {

    private long memberId;
    private Role role;

    private TokenCreationInfo(long memberId, Role role) {
        this.memberId = memberId;
        this.role = role;
    }

    public static TokenCreationInfo of(Member member) {
        return new TokenCreationInfo(member.getId(), member.getRole());
    }
}
