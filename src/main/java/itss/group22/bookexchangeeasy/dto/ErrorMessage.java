package itss.group22.bookexchangeeasy.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorMessage {
    private Date timestamp;
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
        this.timestamp = new Date();
    }
}
