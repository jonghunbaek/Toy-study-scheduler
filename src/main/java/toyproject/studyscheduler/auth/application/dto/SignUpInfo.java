package toyproject.studyscheduler.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import toyproject.studyscheduler.member.domain.entity.Member;
import toyproject.studyscheduler.member.domain.Role;

@NoArgsConstructor
@Getter
public class SignUpInfo {

    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    private String password;
    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    private String name;

    @Builder
    private SignUpInfo(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .name(name)
            .role(Role.ROLE_USER)
            .build();
    }
}
