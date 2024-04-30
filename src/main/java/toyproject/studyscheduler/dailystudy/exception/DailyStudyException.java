package toyproject.studyscheduler.dailystudy.exception;

import toyproject.studyscheduler.common.exception.GlobalException;
import toyproject.studyscheduler.common.response.ResponseCode;

public class DailyStudyException extends GlobalException {

    public DailyStudyException(ResponseCode responseCode) {
        super(responseCode);
    }

    public DailyStudyException(String detailMessage, ResponseCode responseCode) {
        super(detailMessage, responseCode);
    }
}
