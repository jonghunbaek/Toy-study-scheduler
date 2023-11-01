package toyproject.studyscheduler.api.controller.request.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.member.AccountType;

@NoArgsConstructor
@Getter
public class SaveMemberRequestDto {

    private String email;
    private String password;
    private String name;
    private AccountType accountType;

    @Builder
    private SaveMemberRequestDto(String email, String password, String name, AccountType accountType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.accountType = accountType;
    }
}
