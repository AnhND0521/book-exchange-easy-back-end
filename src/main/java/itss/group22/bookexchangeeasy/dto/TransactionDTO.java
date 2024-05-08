package itss.group22.bookexchangeeasy.dto;

import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.ExchangeItemType;
import itss.group22.bookexchangeeasy.enums.TransactionStatus;
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
