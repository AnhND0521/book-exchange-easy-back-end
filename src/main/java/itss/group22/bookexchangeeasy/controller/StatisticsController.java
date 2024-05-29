package itss.group22.bookexchangeeasy.controller;
import io.swagger.v3.oas.annotations.Operation;
import itss.group22.bookexchangeeasy.dto.statistics.LineChartItem;
import itss.group22.bookexchangeeasy.dto.statistics.OverviewStatistics;
import itss.group22.bookexchangeeasy.dto.statistics.PieChartItem;
import itss.group22.bookexchangeeasy.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/overview")
    @Operation(summary = "Lấy các thông số tổng quan")
    public ResponseEntity<OverviewStatistics> getOverviewStatistics() {
        return ResponseEntity.ok(statisticsService.getOverviewStatistics());
    }

    @GetMapping("/charts/transactions-by-status")
    @Operation(summary = "Thống kê phần trăm giao dịch ở từng trạng thái")
    public ResponseEntity<List<PieChartItem>> getTransactionPercentagesByStatus(
            @RequestParam(name = "filter-by", required = false) String filterBy
    ) {
        return ResponseEntity.ok(statisticsService.getTransactionPercentagesByStatus(filterBy));
    }

    @GetMapping("/charts/new-books-by-date")
    @Operation(summary = "Thống kê số sách mới theo từng ngày")
    public ResponseEntity<List<LineChartItem>> getNewBookCountByDate(
            @RequestParam(name = "from", required = false) LocalDate from,
            @RequestParam(name = "from", required = false) LocalDate to
    ) {
        return ResponseEntity.ok(statisticsService.getNewBookCountByDate(from, to));
    }

    @GetMapping("/charts/transactions-by-date")
    @Operation(summary = "Thống kê số giao dịch theo từng ngày")
    public ResponseEntity<List<LineChartItem>> getTransactionCountByDate(
            @RequestParam(name = "from", required = false) LocalDate from,
            @RequestParam(name = "to", required = false) LocalDate to
    ) {
        return ResponseEntity.ok(statisticsService.getTransactionCountByDate(from, to));
    }
}
