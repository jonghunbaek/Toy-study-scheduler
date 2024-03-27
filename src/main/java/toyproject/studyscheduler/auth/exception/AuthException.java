package toyproject.studyscheduler.auth.exception;

import toyproject.studyscheduler.common.exception.GlobalException;
import toyproject.studyscheduler.common.response.ResponseCode;

public class AuthException extends GlobalException {

    public AuthException(ResponseCode responseCode) {
        super(responseCode);
    }

    public AuthException(String message, ResponseCode responseCode) {
        super(message, responseCode);
    }
}
