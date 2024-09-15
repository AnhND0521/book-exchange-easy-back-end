package itss.group22.bookexchangeeasy.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    public static final ApiException KEY_NOT_VALID = new ApiException("Key is not valid", 1415, HttpStatus.FORBIDDEN);
    public static final ApiException ACCOUNT_NOT_ACTIVATED = new ApiException("Account has not been activated", 1404, HttpStatus.BAD_REQUEST);
    public static final ApiException ACCOUNT_ALREADY_ACTIVATED = new ApiException("Account has already been activated", 1405, HttpStatus.BAD_REQUEST);

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
