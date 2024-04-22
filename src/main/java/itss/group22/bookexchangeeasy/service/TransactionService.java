package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.ExchangeRequestDTO;

import java.util.List;

public interface TransactionService {
    void requestExchange(Long bookId, ExchangeRequestDTO requestDTO);
    List<ExchangeRequestDTO> getRequestsOfBook(Long bookId);
    void acceptRequest(Long bookId, Long requestId);
    void rejectRequest(Long bookId, Long requestId);
}
