package toyproject.studyscheduler.common.exception;

import lombok.Getter;
import toyproject.studyscheduler.common.response.ResponseCode;

@Getter
public class GlobalException extends RuntimeException {

    public static final String DETAIL_DELIMITER = " detail => ";

    private String detailMessage;
    private ResponseCode responseCode;

    public GlobalException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public GlobalException(String detailMessage, ResponseCode responseCode) {
        super(responseCode.getMessage() + DETAIL_DELIMITER + detailMessage);
        this.detailMessage = detailMessage;
        this.responseCode = responseCode;
    }
}
