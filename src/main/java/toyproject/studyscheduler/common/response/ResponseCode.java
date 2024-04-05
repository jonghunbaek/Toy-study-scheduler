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
    E00001("토큰이 존재하지 않거나 허가되지 않은 접근입니다.", UNAUTHORIZED),
    E00002("해당 페이지에 접근 권한이 없습니다.", FORBIDDEN),
    E00003("토큰이 만료됐습니다.", UNAUTHORIZED),
    E00004("토큰이 위조됐습니다.", UNAUTHORIZED),
    E00005("해당 토큰은 사용 불가한 토큰입니다.", UNAUTHORIZED),
    E00006("토큰의 형식이 잘못됐습니다.", UNAUTHORIZED),

    // member
    E10000("비밀번호가 일치하지 않습니다.", BAD_REQUEST),
    E10001("이미 존재하는 이메일이 있습니다.", BAD_REQUEST),

    // token
    E20000("일치하는 리프레쉬 토큰이 없습니다.", BAD_REQUEST),

    // study
    E30000("해당 학습은 이미 종료되었습니다.", BAD_REQUEST),

    // input validation
    E90000("잘못된 입력 값이 존재합니다.", BAD_REQUEST);

    private String message;
    private HttpStatus httpStatus;
}
