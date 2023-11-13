package toyproject.studyscheduler.api.controller.request.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.studyscheduler.domain.member.AccountType;
import toyproject.studyscheduler.domain.member.Member;

@NoArgsConstructor
@Getter
public class SaveMemberRequestDto {

    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    private String password;
    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    private String name;
    private AccountType accountType;

    @Builder
    private SaveMemberRequestDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.accountType = AccountType.ACTIVE;
    }

    public Member toEntity() {
        return Member.builder()
            .email(email)
            .password(password)
            .name(name)
            .build();
    }
}
