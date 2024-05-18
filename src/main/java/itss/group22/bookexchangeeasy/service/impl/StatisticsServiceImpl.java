package itss.group22.bookexchangeeasy.service.impl;
import itss.group22.bookexchangeeasy.dto.statistics.LineChartItem;
import itss.group22.bookexchangeeasy.dto.statistics.OverviewStatistics;
import itss.group22.bookexchangeeasy.dto.statistics.PieChartItem;
import itss.group22.bookexchangeeasy.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    @Override
    public OverviewStatistics getOverviewStatistics() {
        return OverviewStatistics.builder()
//                .allBooks()
//                .newBooksThisMonth()
//                .allTransactions()
//                .transactionsThisMonth()
//                .allUsers()
//                .newUsersThisMonth()
//                .allEvents()
//                .eventsThisMonth()
                .build();
    }

    @Override
    public List<PieChartItem> getTransactionPercentagesByStatus(String filterBy) {
        return null;
    }

    @Override
    public List<LineChartItem> getNewBookCountByDate(LocalDate from, LocalDate to) {
        return null;
    }

    @Override
    public List<LineChartItem> getTransactionCountByDate(LocalDate from, LocalDate to) {
        return null;
    }
}
