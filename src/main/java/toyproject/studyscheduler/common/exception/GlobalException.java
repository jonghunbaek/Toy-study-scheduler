package toyproject.studyscheduler.common.exception;

public class GlobalException extends RuntimeException {

    private String message;
    private ResponseCode responseCode;

    public GlobalException(String message, ResponseCode responseCode) {
        super(responseCode.getMessage() + " detail => " + message);
        this.message = message;
        this.responseCode = responseCode;
    }
}
