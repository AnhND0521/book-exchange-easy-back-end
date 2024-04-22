package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.ExchangeRequestDTO;

import java.util.List;

public interface TransactionService {
    void requestExchange(Long bookId, ExchangeRequestDTO requestDTO);
    List<ExchangeRequestDTO> getRequestsOfBook(Long bookId);
}
