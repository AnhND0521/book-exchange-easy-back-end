package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.book.ExchangeOfferDTO;
import itss.group22.bookexchangeeasy.dto.book.TransactionDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    void offerExchange(Long bookId, ExchangeOfferDTO offerDTO);
    List<ExchangeOfferDTO> getOffersOfBook(Long bookId);
    void acceptOffer(Long bookId, Long offerId);
    void rejectOffer(Long bookId, Long offerId);
    Page<TransactionDTO> getTransactions(int page, int size);
    Page<TransactionDTO> getTransactionsByUser(Long userId, int page, int size);
    TransactionDTO getTransactionDetails(String id);
    Page<TransactionDTO> searchTransactions(String keyword, int page, int size);
    void updateTransactionStatus(String id, String statusName);
}
