package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.BookDTO;
import itss.group22.bookexchangeeasy.dto.ExchangeRequestDTO;
import itss.group22.bookexchangeeasy.dto.MoneyItemDTO;
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

        request.setStatus(ExchangeRequestStatus.ACCEPTED);
        exchangeRequestRepository.save(request);

        Transaction transaction = mapper.map(request, Transaction.class);
        transaction.setStatus(TransactionStatus.CONFIRMED);
        transactionRepository.save(transaction);

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

        request.setStatus(ExchangeRequestStatus.REJECTED);
        exchangeRequestRepository.save(request);

        // notify user
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
}
