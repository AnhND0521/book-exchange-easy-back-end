package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.ExchangeRequestDTO;

public interface TransactionService {
    void createRequest(ExchangeRequestDTO requestDTO);
}
