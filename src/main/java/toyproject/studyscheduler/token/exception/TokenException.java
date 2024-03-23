package toyproject.studyscheduler.token.exception;

import toyproject.studyscheduler.common.exception.GlobalException;
import toyproject.studyscheduler.common.exception.ResponseCode;

public class TokenException extends GlobalException {

    public TokenException(ResponseCode responseCode) {
        super(responseCode);
    }

    public TokenException(String message, ResponseCode responseCode) {
        super(message, responseCode);
    }
}
