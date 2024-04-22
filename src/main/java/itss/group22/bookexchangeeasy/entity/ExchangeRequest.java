package itss.group22.bookexchangeeasy.entity;

import itss.group22.bookexchangeeasy.enums.ExchangeItemType;
import itss.group22.bookexchangeeasy.enums.ExchangeRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @ManyToOne
    @JoinColumn(name = "target_book_id")
    private Book targetBook;

    @Enumerated
    private ExchangeItemType exchangeItemType;

    @OneToOne
    @JoinColumn(name = "book_item_id")
    private Book bookItem;

    @OneToOne
    @JoinColumn(name = "money_item_id")
    private MoneyItem moneyItem;

    @Enumerated
    private ExchangeRequestStatus status;
}
