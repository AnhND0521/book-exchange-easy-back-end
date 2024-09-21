package itss.group22.bookexchangeeasy.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    public static final ApiException KEY_NOT_VALID = new ApiException("Key is not valid", 1415, HttpStatus.FORBIDDEN);
    public static final ApiException ACCOUNT_NOT_ACTIVATED = new ApiException("Account has not been activated", 1416, HttpStatus.BAD_REQUEST);
    public static final ApiException ACCOUNT_ALREADY_ACTIVATED = new ApiException("Account has already been activated", 1417, HttpStatus.BAD_REQUEST);
    public static final ApiException USER_ALREADY_LIKED_COMMENT = new ApiException("User already liked this comment", 1418, HttpStatus.BAD_REQUEST);
    public static final ApiException USER_DID_NOT_LIKE_COMMENT = new ApiException("User did not like this comment", 1419, HttpStatus.BAD_REQUEST);
    private HttpStatus status;
    private Integer errorCode;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ApiException(String message, Integer errorCode, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
