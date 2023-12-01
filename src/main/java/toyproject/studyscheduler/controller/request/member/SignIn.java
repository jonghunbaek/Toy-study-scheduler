package toyproject.studyscheduler.controller.request.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignIn {

    @NotBlank
    private String email;
    @NotBlank
    private String password;

    private SignIn(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static SignIn of(String email, String password) {
        return new SignIn(email, password);
    }
}
