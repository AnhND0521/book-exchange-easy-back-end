package itss.group22.bookexchangeeasy.dto.common;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorMessage {
    private Date timestamp;
    private Integer errorCode;
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

    public ErrorMessage(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = new Date();
    }
}
