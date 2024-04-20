package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.BookDTO;
import itss.group22.bookexchangeeasy.dto.ExchangeRequestDTO;
import itss.group22.bookexchangeeasy.entity.Book;
import itss.group22.bookexchangeeasy.entity.ExchangeRequest;
import itss.group22.bookexchangeeasy.entity.MoneyItem;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.BookStatus;
import itss.group22.bookexchangeeasy.enums.ExchangeItemType;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.BookRepository;
import itss.group22.bookexchangeeasy.repository.ExchangeRequestRepository;
import itss.group22.bookexchangeeasy.repository.MoneyItemRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import itss.group22.bookexchangeeasy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class TransactionServiceImpl implements TransactionService {
private final UserRepository userRepository;
private final BookRepository bookRepository;
private final ModelMapper mapper;
private final MoneyItemRepository moneyItemRepository;
private final ExchangeRequestRepository exchangeRequestRepository;

    @Override
    public void createRequest(ExchangeRequestDTO requestDTO) {
        ExchangeRequest request = toEntity(requestDTO);
        if(request.getBookItem()!=null){
            bookRepository.save(request.getBookItem());
        }if(request.getMoneyItem()!=null){
            moneyItemRepository.save(request.getMoneyItem());
        }
        exchangeRequestRepository.save(request);
    }
   private ExchangeRequest toEntity(ExchangeRequestDTO requestDTO) {
        User borrower = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User","id",requestDTO.getUserId()));
        Book targetBook = bookRepository.findById(requestDTO.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book","id",requestDTO.getBookId()));
        User owner = targetBook.getOwner();
       ExchangeItemType exchangeItemType = ExchangeItemType.valueOf(requestDTO.getExchangeType());
       Book bookItem = null;
       MoneyItem moneyItem = null;
       if(exchangeItemType == ExchangeItemType.BOOK) {
           bookItem = mapper.map(requestDTO.getBookItem(), Book.class);
           bookItem.setOwner(borrower);
           bookItem.setStatus(BookStatus.EXCHANGED);
       }else{
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

}
