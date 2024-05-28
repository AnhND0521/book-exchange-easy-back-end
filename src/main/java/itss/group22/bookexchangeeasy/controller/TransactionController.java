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

    @PostMapping("/books/{bookId}/offers")
    @Operation(
            summary = "Gửi yêu cầu trao đổi sách",
            description = "Các trường cần nhập gồm " +
                    "userId (id của người yêu cầu), " +
                    "bookId (id của sách muốn yêu cầu), " +
                    "exchangeItemType (kiểu trao đổi bằng sách hay tiền, nhận một trong hai giá trị 'BOOK' hoặc 'MONEY'), " +
                    "bookItem (cuốn sách dùng để trao đổi trong trường hợp đổi bằng sách) hoặc " +
                    "moneyItem (lượng tiền dùng để trao đổi trong trường hợp đổi bằng tiền)"
    )
    public ResponseEntity<ResponseMessage> offerExchange(@PathVariable Long bookId, @RequestBody ExchangeOfferDTO offerDTO) {
        transactionService.offerExchange(bookId, offerDTO);
        return ResponseEntity.ok(new ResponseMessage("Offer created successfully"));
    }

    @GetMapping("/books/{bookId}/offers")
    @Operation(summary = "Lấy danh sách tất cả yêu cầu trao đổi của một cuốn sách")
    public ResponseEntity<List<ExchangeOfferDTO>> getOffersOfBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(transactionService.getOffersOfBook(bookId));
    }

    @PostMapping("/books/{bookId}/offers/{offerId}/accept")
    @Operation(summary = "Chấp nhận một yêu cầu trao đôi")
    public ResponseEntity<ResponseMessage> acceptOffer(@PathVariable Long bookId, @PathVariable Long offerId) {
        transactionService.acceptOffer(bookId, offerId);
        return ResponseEntity.ok(new ResponseMessage("Offer accepted successfully"));
    }

    @PostMapping("/books/{bookId}/offers/{offerId}/reject")
    @Operation(summary = "Từ chối một yêu cầu trao đổi")
    public ResponseEntity<ResponseMessage> rejectOffer(@PathVariable Long bookId, @PathVariable Long offerId) {
        transactionService.rejectOffer(bookId, offerId);
        return ResponseEntity.ok(new ResponseMessage("Offer rejected successfully"));
    }

    @PostMapping("/books/{bookId}/offers/accept-earliest")
    @Operation(summary = "Chấp nhận yêu cầu trao đổi sớm nhất")
    public ResponseEntity<ResponseMessage> acceptEarliestOffer(@PathVariable Long bookId) {
        transactionService.acceptEarliestOffer(bookId);
        return ResponseEntity.ok(new ResponseMessage("Earliest offer accepted successfully"));
    }

    @PostMapping("/books/{bookId}/offers/reject-all")
    @Operation(summary = "Từ chối tất cả yêu cầu trao đổi")
    public ResponseEntity<ResponseMessage> rejectAllOffers(@PathVariable Long bookId) {
        transactionService.rejectAllOffers(bookId);
        return ResponseEntity.ok(new ResponseMessage("All offers rejected successfully"));
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

    @GetMapping("/transactions/{id}")
    @Operation(summary = "Lấy thông tin chi tiết của một giao dịch")
    public ResponseEntity<TransactionDTO> getTransactionDetails(@PathVariable String id) {
        return ResponseEntity.ok(transactionService.getTransactionDetails(id));
    }

    @GetMapping("/transactions/search")
    @Operation(summary = "Tìm kiếm giao dịch")
    public ResponseEntity<Page<TransactionDTO>> searchTransactions(
            @RequestParam(name = "q", required = true) String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        return ResponseEntity.ok(transactionService.searchTransactions(keyword, page, size));
    }

    @PutMapping("/transactions/{id}/update-status")
    @Operation(
            summary = "Cập nhật trạng thái giao dịch",
            description = "Giao dịch mang một trong các trạng thái 'CONFIRMED', 'DELIVERING', 'COMPLETED' hoặc 'CANCELLED'. " +
                    "Nếu muốn đổi thành một trạng thái tùy ý thì điền tên trạng thái vào tham số value. " +
                    "Nếu muốn tự động cập nhật thành trạng thái tiếp theo thì không cần điền tham số này"
    )
    public ResponseEntity<ResponseMessage> updateTransactionStatus(
            @PathVariable String id,
            @RequestParam(name = "value", required = false) String statusName
    ) {
        transactionService.updateTransactionStatus(id, statusName);
        return ResponseEntity.ok(new ResponseMessage("Transaction status updated successfully"));
    }
}
