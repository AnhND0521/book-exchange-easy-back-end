package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.book.ExchangeRequestDTO;
import itss.group22.bookexchangeeasy.dto.book.TransactionDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    void requestExchange(Long bookId, ExchangeRequestDTO requestDTO);
    List<ExchangeRequestDTO> getRequestsOfBook(Long bookId);
    void acceptRequest(Long bookId, Long requestId);
    void rejectRequest(Long bookId, Long requestId);
    Page<TransactionDTO> getTransactions(int page, int size);
    Page<TransactionDTO> getTransactionsByUser(Long userId, int page, int size);
}
