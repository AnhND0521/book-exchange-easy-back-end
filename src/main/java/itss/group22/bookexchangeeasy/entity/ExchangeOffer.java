package itss.group22.bookexchangeeasy.entity;

import itss.group22.bookexchangeeasy.enums.ExchangeItemType;
import itss.group22.bookexchangeeasy.enums.ExchangeOfferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_offer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeOffer {
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

    @ManyToOne
    @JoinColumn(name = "book_item_id")
    private Book bookItem;

    @OneToOne
    @JoinColumn(name = "money_item_id")
    private MoneyItem moneyItem;

    @Enumerated
    private ExchangeOfferStatus status;
}
