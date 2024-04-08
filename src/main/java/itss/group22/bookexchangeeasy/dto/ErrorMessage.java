package itss.group22.bookexchangeeasy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ErrorMessage {
    private Date timestamp;
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
        this.timestamp = new Date();
    }
}
