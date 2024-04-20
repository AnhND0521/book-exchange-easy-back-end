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
    @PostMapping("/transactions/request")
    public ResponseEntity<ResponseMessage> createRequest(@RequestBody ExchangeRequestDTO requestDTO) {
        return ResponseEntity.ok(new ResponseMessage("Request created successfully"));
    }
    @GetMapping("/transactions/request/{bookId}")
    public ResponseEntity<List<ExchangeRequestDTO>> getRequestsOfBook(@PathVariable Long bookId) {

        return null;

    }
}
