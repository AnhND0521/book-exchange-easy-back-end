package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.BookDTO;
import itss.group22.bookexchangeeasy.dto.ExchangeRequestDTO;
import itss.group22.bookexchangeeasy.dto.MoneyItemDTO;
import itss.group22.bookexchangeeasy.dto.TransactionDTO;
import itss.group22.bookexchangeeasy.entity.*;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import itss.group22.bookexchangeeasy.enums.ExchangeItemType;
import itss.group22.bookexchangeeasy.enums.ExchangeRequestStatus;
import itss.group22.bookexchangeeasy.enums.TransactionStatus;
import itss.group22.bookexchangeeasy.exception.ApiException;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.*;
import itss.group22.bookexchangeeasy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class TransactionServiceImpl implements TransactionService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ModelMapper mapper;
    private final MoneyItemRepository moneyItemRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public void requestExchange(Long bookId, ExchangeRequestDTO requestDTO) {
        requestDTO.setBookId(bookId);
        ExchangeRequest request = toEntity(requestDTO);
        if (!request.getTargetBook().getStatus().equals(BookStatus.AVAILABLE))
            throw new ApiException("Book is not available");

        request.setStatus(ExchangeRequestStatus.PENDING);
        if (request.getBookItem() != null) {
            bookRepository.save(request.getBookItem());
        }
        if (request.getMoneyItem() != null) {
            moneyItemRepository.save(request.getMoneyItem());
        }
        exchangeRequestRepository.save(request);

        // notify owner
    }

    @Override
    public List<ExchangeRequestDTO> getRequestsOfBook(Long bookId) {
        return exchangeRequestRepository.findByTargetBookIdOrderByTimestampAsc(bookId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public void acceptRequest(Long bookId, Long requestId) {
        ExchangeRequest request = exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request", "id", requestId));

        if (!request.getTargetBook().getId().equals(bookId))
            throw new ApiException("Exchange request does not belong to specified book");

        if (!request.getStatus().equals(ExchangeRequestStatus.PENDING))
            throw new ApiException("Exchange request has already been handled");

        request.setStatus(ExchangeRequestStatus.ACCEPTED);
        exchangeRequestRepository.save(request);

        Transaction transaction = mapper.map(request, Transaction.class);
        transaction.setStatus(TransactionStatus.CONFIRMED);
        transactionRepository.save(transaction);

        transaction.getTargetBook().setStatus(BookStatus.EXCHANGED);
        bookRepository.save(transaction.getTargetBook());

        // auto reject other requests
        exchangeRequestRepository
                .findByTargetBookIdOrderByTimestampAsc(bookId)
                .forEach(req -> {
                    if (req.getId().equals(requestId)) return;
                    req.setStatus(ExchangeRequestStatus.REJECTED);
                    exchangeRequestRepository.save(req);

                    // notify user
                });
    }

    @Override
    public void rejectRequest(Long bookId, Long requestId) {
        ExchangeRequest request = exchangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Exchange request", "id", requestId));

        if (!request.getTargetBook().getId().equals(bookId))
            throw new ApiException("Exchange request does not belong to specified book");

        if (!request.getStatus().equals(ExchangeRequestStatus.PENDING))
            throw new ApiException("Exchange request has already been handled");

        request.setStatus(ExchangeRequestStatus.REJECTED);
        exchangeRequestRepository.save(request);

        // notify user
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

    private ExchangeRequest toEntity(ExchangeRequestDTO requestDTO) {
        Book targetBook = bookRepository.findById(requestDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", requestDTO.getBookId()));
        User borrower = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", requestDTO.getUserId()));
        User owner = targetBook.getOwner();
        ExchangeItemType exchangeItemType = ExchangeItemType.valueOf(requestDTO.getExchangeItemType());
        Book bookItem = null;
        MoneyItem moneyItem = null;
        if (exchangeItemType == ExchangeItemType.BOOK) {
            bookItem = mapper.map(requestDTO.getBookItem(), Book.class);
            bookItem.setOwner(borrower);
            bookItem.setStatus(BookStatus.EXCHANGED);
        } else {
            moneyItem = mapper.map(requestDTO.getMoneyItem(), MoneyItem.class);
        }
        return ExchangeRequest.builder()
                .owner(owner)
                .borrower(borrower)
                .targetBook(targetBook)
                .exchangeItemType(exchangeItemType)
                .bookItem(bookItem)
                .moneyItem(moneyItem)
                .build();
    }

    private ExchangeRequestDTO toDTO(ExchangeRequest request) {
        return ExchangeRequestDTO.builder()
                .id(request.getId())
                .userId(request.getBorrower().getId())
                .bookId(request.getTargetBook().getId())
                .exchangeItemType(request.getExchangeItemType().name())
                .bookItem(request.getBookItem() == null ? null : mapper.map(request.getBookItem(), BookDTO.class))
                .moneyItem(request.getMoneyItem() == null ? null : mapper.map(request.getMoneyItem(), MoneyItemDTO.class))
                .status(request.getStatus().name())
                .timestamp(request.getTimestamp())
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
