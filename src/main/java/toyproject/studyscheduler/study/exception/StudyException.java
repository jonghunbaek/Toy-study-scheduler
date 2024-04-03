package toyproject.studyscheduler.study.exception;

import toyproject.studyscheduler.common.exception.GlobalException;
import toyproject.studyscheduler.common.response.ResponseCode;

public class StudyException extends GlobalException {

    public StudyException(ResponseCode responseCode) {
        super(responseCode);
    }

    public StudyException(String message, ResponseCode responseCode) {
        super(message, responseCode);
    }
}
