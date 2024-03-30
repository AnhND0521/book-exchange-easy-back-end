package itss.group22.bookexchangeeasy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ResponseMessage {
    private Date timestamp;
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
        this.timestamp = new Date();
    }
}