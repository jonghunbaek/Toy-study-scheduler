package toyproject.studyscheduler.member.exception;

import toyproject.studyscheduler.common.exception.GlobalException;
import toyproject.studyscheduler.common.response.ResponseCode;

public class MemberException extends GlobalException {

    public MemberException(ResponseCode responseCode) {
        super(responseCode);
    }

    public MemberException(String message, ResponseCode responseCode) {
        super(message, responseCode);
    }
}
