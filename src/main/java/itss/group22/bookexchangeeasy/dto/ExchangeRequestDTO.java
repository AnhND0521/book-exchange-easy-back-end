package itss.group22.bookexchangeeasy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ExchangeRequestDTO {
    private Long userId;
    private Long bookId;
    private String exchangeType;
    private BookDTO bookItem;
    private MoneyItemDTO moneyItem;
}
