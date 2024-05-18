package itss.group22.bookexchangeeasy.controller;

import io.swagger.v3.oas.annotations.Operation;
import itss.group22.bookexchangeeasy.dto.book.ExchangeOfferDTO;
import itss.group22.bookexchangeeasy.dto.common.ResponseMessage;
import itss.group22.bookexchangeeasy.dto.book.TransactionDTO;
import itss.group22.bookexchangeeasy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/books/{bookId}/requests")
    @Operation(
            summary = "Gửi yêu cầu trao đổi sách",
            description = "Các trường cần nhập gồm " +
                    "userId (id của người yêu cầu), " +
                    "bookId (id của sách muốn yêu cầu), " +
                    "exchangeItemType (kiểu trao đổi bằng sách hay tiền, nhận một trong hai giá trị 'BOOK' hoặc 'MONEY'), " +
                    "bookItem (cuốn sách dùng để trao đổi trong trường hợp đổi bằng sách) hoặc " +
                    "moneyItem (lượng tiền dùng để trao đổi trong trường hợp đổi bằng tiền)"
    )
    public ResponseEntity<ResponseMessage> requestExchange(@PathVariable Long bookId, @RequestBody ExchangeOfferDTO requestDTO) {
        transactionService.requestExchange(bookId, requestDTO);
        return ResponseEntity.ok(new ResponseMessage("Request created successfully"));
    }

    @GetMapping("/books/{bookId}/requests")
    @Operation(summary = "Lấy danh sách tất cả yêu cầu trao đổi của một cuốn sách")
    public ResponseEntity<List<ExchangeOfferDTO>> getRequestsOfBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(transactionService.getRequestsOfBook(bookId));
    }

    @PostMapping("/books/{bookId}/requests/{requestId}/accept")
    @Operation(summary = "Chấp nhận một yêu cầu trao đôi")
    public ResponseEntity<ResponseMessage> acceptRequest(@PathVariable Long bookId, @PathVariable Long requestId) {
        transactionService.acceptRequest(bookId, requestId);
        return ResponseEntity.ok(new ResponseMessage("Request accepted successfully"));
    }

    @PostMapping("/books/{bookId}/requests/{requestId}/reject")
    @Operation(summary = "Từ chối một yêu cầu trao đổi")
    public ResponseEntity<ResponseMessage> rejectRequest(@PathVariable Long bookId, @PathVariable Long requestId) {
        transactionService.rejectRequest(bookId, requestId);
        return ResponseEntity.ok(new ResponseMessage("Request rejected successfully"));
    }

    @GetMapping("/transactions")
    @Operation(summary = "Lấy danh sách toàn bộ các giao dịch (có phân trang)")
    public ResponseEntity<Page<TransactionDTO>> getTransactions(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(transactionService.getTransactions(page, size));
    }

    @GetMapping("/transactions/find-by-user")
    @Operation(summary = "Lấy danh sách các giao dịch của một người dùng (có phân trang)")
    public ResponseEntity<Page<TransactionDTO>> getTransactionsByUser(
            @RequestParam(name = "id", required = true) Long userId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId, page, size));
    }
}
