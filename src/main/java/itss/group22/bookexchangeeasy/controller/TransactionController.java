package itss.group22.bookexchangeeasy.controller;

import itss.group22.bookexchangeeasy.dto.ExchangeRequestDTO;
import itss.group22.bookexchangeeasy.dto.ResponseMessage;
import itss.group22.bookexchangeeasy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/books/{bookId}/requests")
    public ResponseEntity<ResponseMessage> requestExchange(@PathVariable Long bookId, @RequestBody ExchangeRequestDTO requestDTO) {
        transactionService.requestExchange(bookId, requestDTO);
        return ResponseEntity.ok(new ResponseMessage("Request created successfully"));
    }

    @GetMapping("/books/{bookId}/requests")
    public ResponseEntity<List<ExchangeRequestDTO>> getRequestsOfBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(transactionService.getRequestsOfBook(bookId));
    }

    @PostMapping("/books/{bookId}/requests/{requestId}/accept")
    public ResponseEntity<ResponseMessage> acceptRequest(@PathVariable Long bookId, @PathVariable Long requestId) {
        transactionService.acceptRequest(bookId, requestId);
        return ResponseEntity.ok(new ResponseMessage("Request accepted successfully"));
    }

    @PostMapping("/books/{bookId}/requests/{requestId}/reject")
    public ResponseEntity<ResponseMessage> rejectRequest(@PathVariable Long bookId, @PathVariable Long requestId) {
        transactionService.rejectRequest(bookId, requestId);
        return ResponseEntity.ok(new ResponseMessage("Request rejected successfully"));
    }
}
