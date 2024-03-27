package toyproject.studyscheduler.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    // auth
    E00000("OK", OK),
    E00001("해당 페이지에 접근 권한이 없습니다.", FORBIDDEN),

    // member
    E10000("비밀번호가 일치하지 않습니다.", BAD_REQUEST),
    E10001("이미 존재하는 이메일이 있습니다.", BAD_REQUEST),

    // token
    E20000("일치하는 리프레쉬 토큰이 없습니다.", BAD_REQUEST);

    private String message;
    private HttpStatus httpStatus;
}
