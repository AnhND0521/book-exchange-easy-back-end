package itss.group22.bookexchangeeasy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ExchangeRequestDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private String exchangeItemType;
    private BookDTO bookItem;
    private MoneyItemDTO moneyItem;
    private String status;
    private LocalDateTime timestamp;
}
