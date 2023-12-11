package toyproject.studyscheduler.controller.request.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;

    private SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static SignInRequest of(String email, String password) {
        return new SignInRequest(email, password);
    }
}
