package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.book.BookDTO;
import itss.group22.bookexchangeeasy.dto.book.ExchangeOfferDTO;
import itss.group22.bookexchangeeasy.dto.book.MoneyItemDTO;
import itss.group22.bookexchangeeasy.dto.book.TransactionDTO;
import itss.group22.bookexchangeeasy.entity.*;
import itss.group22.bookexchangeeasy.enums.*;
import itss.group22.bookexchangeeasy.exception.ApiException;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.*;
import itss.group22.bookexchangeeasy.service.NotificationService;
import itss.group22.bookexchangeeasy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final MoneyItemRepository moneyItemRepository;
    private final ExchangeOfferRepository exchangeOfferRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;
    private final ModelMapper mapper;

    @Override
    public void offerExchange(Long bookId, ExchangeOfferDTO offerDTO) {
        offerDTO.setBookId(bookId);
        ExchangeOffer offer = toEntity(offerDTO);

        if (!offer.getTargetBook().getStatus().equals(BookStatus.AVAILABLE))
            throw new ApiException("Book is not available");

        offer.setStatus(ExchangeOfferStatus.PENDING);
        if (offer.getBookItem() != null) {
            bookRepository.save(offer.getBookItem());
        }
        if (offer.getMoneyItem() != null) {
            moneyItemRepository.save(offer.getMoneyItem());
        }
        exchangeOfferRepository.save(offer);

        // notify owner
        notificationService.sendNotification(Notification.builder()
                .user(offer.getOwner())
                .content("You've got a new offer on book '" + offer.getTargetBook().getTitle()
                        + "' from " + offer.getBorrower().getName())
                .href("book/" + offer.getTargetBook().getId())
                .type(NotificationType.INFORMATION)
                .build());
    }

    @Override
    public List<ExchangeOfferDTO> getOffersOfBook(Long bookId) {
        return exchangeOfferRepository.findByTargetBookIdAndStatusOrderByTimestampAsc(bookId, ExchangeOfferStatus.PENDING)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public void acceptOffer(Long bookId, Long offerId) {
        ExchangeOffer offer = exchangeOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange offer", "id", offerId));

        if (!offer.getTargetBook().getId().equals(bookId))
            throw new ApiException("Exchange offer does not belong to specified book");

        if (!offer.getStatus().equals(ExchangeOfferStatus.PENDING))
            throw new ApiException("Exchange offer has already been handled");

        offer.setStatus(ExchangeOfferStatus.ACCEPTED);
        exchangeOfferRepository.save(offer);

        Transaction transaction = mapper.map(offer, Transaction.class);
        transaction.setStatus(TransactionStatus.CONFIRMED);
        transactionRepository.save(transaction);

        transaction.getTargetBook().setStatus(BookStatus.EXCHANGED);
        bookRepository.save(transaction.getTargetBook());

        if (transaction.getBookItem() != null) {
            transaction.getBookItem().setStatus(BookStatus.EXCHANGED);
            bookRepository.save(transaction.getBookItem());
        }

        // notify acceptance
        notificationService.sendNotification(Notification.builder()
                .user(offer.getBorrower())
                .content("Your offer on book '" + offer.getTargetBook().getTitle() + "' has been accepted")
                .href("transaction")
                .type(NotificationType.SUCCESS)
                .build());

        // auto reject other offers
        exchangeOfferRepository
                .findByTargetBookIdAndStatusOrderByTimestampAsc(bookId, ExchangeOfferStatus.PENDING)
                .forEach(req -> {
                    if (req.getId().equals(offerId)) return;
                    req.setStatus(ExchangeOfferStatus.REJECTED);
                    exchangeOfferRepository.save(req);

                    // notify rejection
                    notificationService.sendNotification(Notification.builder()
                            .user(req.getBorrower())
                            .content("Your offer on book '" + req.getTargetBook().getTitle() + "' has been rejected")
                            .href("book/" + req.getTargetBook().getId())
                            .type(NotificationType.ERROR)
                            .build());
                });
    }

    @Override
    public void rejectOffer(Long bookId, Long offerId) {
        ExchangeOffer offer = exchangeOfferRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange offer", "id", offerId));

        if (!offer.getTargetBook().getId().equals(bookId))
            throw new ApiException("Exchange offer does not belong to specified book");

        if (!offer.getStatus().equals(ExchangeOfferStatus.PENDING))
            throw new ApiException("Exchange offer has already been handled");

        offer.setStatus(ExchangeOfferStatus.REJECTED);
        exchangeOfferRepository.save(offer);

        // notify user
        notificationService.sendNotification(Notification.builder()
                .user(offer.getBorrower())
                .content("Your offer on book '" + offer.getTargetBook().getTitle() + "' has been rejected")
                .href("book/" + offer.getTargetBook().getId())
                .type(NotificationType.ERROR)
                .build());
    }

    @Override
    public void acceptEarliestOffer(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        var offers = exchangeOfferRepository.findByTargetBookIdAndStatusOrderByTimestampAsc(bookId, ExchangeOfferStatus.PENDING);
        if (!offers.isEmpty()) {
            acceptOffer(bookId, offers.get(0).getId());
        }
    }

    @Override
    public void rejectAllOffers(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        var offers = exchangeOfferRepository.findByTargetBookIdAndStatusOrderByTimestampAsc(bookId, ExchangeOfferStatus.PENDING);

        offers.forEach(offer -> {
            offer.setStatus(ExchangeOfferStatus.REJECTED);
            exchangeOfferRepository.save(offer);

            // notify user
            notificationService.sendNotification(Notification.builder()
                    .user(offer.getBorrower())
                    .content("Your offer on book '" + offer.getTargetBook().getTitle() + "' has been rejected")
                    .href("book/" + offer.getTargetBook().getId())
                    .type(NotificationType.ERROR)
                    .build());
        });
    }

    @Override
    public Page<TransactionDTO> getTransactions(int page, int size) {
        return transactionRepository
                .findAllByOrderByTimestampDesc(PageRequest.of(page, size))
                .map(this::toDTO);
    }

    @Override
    public Page<TransactionDTO> getTransactionsByUser(Long userId, int page, int size) {
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return transactionRepository
                .findByUserOrderByTimestampDesc(userId, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    @Override
    public TransactionDTO getTransactionDetails(String id) {
        var transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
        return toDTO(transaction);
    }

    @Override
    public Page<TransactionDTO> searchTransactions(String keyword, int page, int size) {
        return transactionRepository
                .findByIdOrOwnerEmailOrBorrowerEmailOrBookTitleLike(keyword, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    @Override
    public void updateTransactionStatus(String id, String statusName) {
        var transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));

        if (statusName != null) {
            TransactionStatus status = TransactionStatus.valueOf(statusName);
            transaction.setStatus(status);
            transactionRepository.save(transaction);
        } else {
            var currentStatus = transaction.getStatus();
            if (currentStatus.equals(TransactionStatus.COMPLETED) || currentStatus.equals(TransactionStatus.CANCELLED)) {
                throw new ApiException("The transaction is already at its last status. You should explicitly specify the next status to update to");
            }
            var nextStatus = switch (currentStatus) {
                case CONFIRMED -> TransactionStatus.DELIVERING;
                case DELIVERING -> TransactionStatus.COMPLETED;
                default -> null;
            };
            transaction.setStatus(nextStatus);
            transactionRepository.save(transaction);
        }
    }

    private ExchangeOffer toEntity(ExchangeOfferDTO offerDTO) {
        Book targetBook = bookRepository.findById(offerDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", offerDTO.getBookId()));
        User borrower = userRepository.findById(offerDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", offerDTO.getUserId()));
        User owner = targetBook.getOwner();
        ExchangeItemType exchangeItemType = ExchangeItemType.valueOf(offerDTO.getExchangeItemType());
        Book bookItem = null;
        MoneyItem moneyItem = null;
        if (exchangeItemType == ExchangeItemType.BOOK) {
            if (offerDTO.getBookItem().getId() != null) {
                bookItem = bookRepository.findById(offerDTO.getBookItem().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Book", "id", offerDTO.getBookItem().getId()));
            } else {
                bookItem = mapper.map(offerDTO.getBookItem(), Book.class);
                bookItem.setOwner(borrower);
                bookItem.setStatus(BookStatus.EXCHANGED);
            }
        } else {
            moneyItem = mapper.map(offerDTO.getMoneyItem(), MoneyItem.class);
        }
        return ExchangeOffer.builder()
                .owner(owner)
                .borrower(borrower)
                .targetBook(targetBook)
                .exchangeItemType(exchangeItemType)
                .bookItem(bookItem)
                .moneyItem(moneyItem)
                .build();
    }

    private ExchangeOfferDTO toDTO(ExchangeOffer offer) {
        return ExchangeOfferDTO.builder()
                .id(offer.getId())
                .userId(offer.getBorrower().getId())
                .bookId(offer.getTargetBook().getId())
                .exchangeItemType(offer.getExchangeItemType().name())
                .bookItem(offer.getBookItem() == null ? null : mapper.map(offer.getBookItem(), BookDTO.class))
                .moneyItem(offer.getMoneyItem() == null ? null : mapper.map(offer.getMoneyItem(), MoneyItemDTO.class))
                .status(offer.getStatus().name())
                .timestamp(offer.getTimestamp())
                .build();
    }

    private TransactionDTO toDTO(Transaction transaction) {
        TransactionDTO dto = mapper.map(transaction, TransactionDTO.class);
        dto.setOwnerId(transaction.getOwner().getId());
        dto.setBorrowerId(transaction.getBorrower().getId());
        dto.setExchangeItemType(transaction.getExchangeItemType().name());

        BookDTO targetBookDTO = mapper.map(transaction.getTargetBook(), BookDTO.class);
        targetBookDTO.setOwnerId(transaction.getTargetBook().getOwner().getId());
        targetBookDTO.setStatus(transaction.getTargetBook().getStatus().name());
        dto.setTargetBook(targetBookDTO);

        switch (transaction.getExchangeItemType()) {
            case BOOK -> {
                BookDTO bookItemDTO = mapper.map(transaction.getBookItem(), BookDTO.class);
                bookItemDTO.setOwnerId(transaction.getBookItem().getOwner().getId());
                bookItemDTO.setStatus(transaction.getBookItem().getStatus().name());
                dto.setBookItem(bookItemDTO);
            }
            case MONEY -> {
                MoneyItemDTO moneyItemDTO = mapper.map(transaction.getMoneyItem(), MoneyItemDTO.class);
                dto.setMoneyItem(moneyItemDTO);
            }
        }

        dto.setStatus(transaction.getStatus().name());
        return dto;
    }
}
