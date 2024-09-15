package itss.group22.bookexchangeeasy.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    public static final ApiException KEY_NOT_VALID_EXCEPTION = new ApiException("Key is not valid", 1415, HttpStatus.FORBIDDEN);

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
