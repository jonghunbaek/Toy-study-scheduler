package toyproject.studyscheduler.controller.request.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequestDto {

    @NotBlank
    private String email;
    @NotBlank
    private String password;

    private SignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static SignInRequestDto of(String email, String password) {
        return new SignInRequestDto(email, password);
    }
}
