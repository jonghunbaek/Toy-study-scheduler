package toyproject.studyscheduler.common.exception;

import toyproject.studyscheduler.common.response.ResponseCode;

public class GlobalException extends RuntimeException {

    private String message;
    private ResponseCode responseCode;

    public GlobalException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public GlobalException(String message, ResponseCode responseCode) {
        super(responseCode.getMessage() + " detail => " + message);
        this.message = message;
        this.responseCode = responseCode;
    }
}
