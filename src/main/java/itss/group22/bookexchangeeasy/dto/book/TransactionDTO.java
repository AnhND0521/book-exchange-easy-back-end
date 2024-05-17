package itss.group22.bookexchangeeasy.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private String id;
    private LocalDateTime timestamp;
    private LocalDateTime lastUpdated;
    private Long ownerId;
    private Long borrowerId;
    private BookDTO targetBook;
    private String exchangeItemType;
    private BookDTO bookItem;
    private MoneyItemDTO moneyItem;
    private String status;
}
