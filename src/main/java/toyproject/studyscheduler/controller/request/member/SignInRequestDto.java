package toyproject.studyscheduler.controller.request.member;

import lombok.Getter;

@Getter
public class SignInRequestDto {

    private String email;
    private String password;

    private SignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static SignInRequestDto of(String email, String password) {
        return new SignInRequestDto(email, password);
    }
}
