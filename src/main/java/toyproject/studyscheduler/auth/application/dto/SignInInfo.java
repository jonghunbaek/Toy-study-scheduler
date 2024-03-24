package toyproject.studyscheduler.auth.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignInInfo {

    private String email;
    private String password;

    public SignInInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
