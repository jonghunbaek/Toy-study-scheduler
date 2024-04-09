package toyproject.studyscheduler.common.resolver;

import lombok.Getter;

@Getter
public class MemberInfo {

    private Long memberId;

    public MemberInfo(Long memberId) {
        this.memberId = memberId;
    }
}
