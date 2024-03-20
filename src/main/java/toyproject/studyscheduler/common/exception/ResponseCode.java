package toyproject.studyscheduler.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    E00000("OK", OK),

    // member
    E10000("비밀번호가 일치하지 않습니다.", BAD_REQUEST),
    E10001("이메일과 일치하는 회원이 존재하지 않습니다.", BAD_REQUEST);

    private String message;
    private HttpStatus httpStatus;
}
