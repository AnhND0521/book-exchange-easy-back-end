package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.statistics.LineChartItem;
import itss.group22.bookexchangeeasy.dto.statistics.OverviewStatistics;
import itss.group22.bookexchangeeasy.dto.statistics.PieChartItem;
import itss.group22.bookexchangeeasy.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    @Override
    public OverviewStatistics getOverviewStatistics() {
        return OverviewStatistics.builder()
                .allBooks(countAllBooks())
                .newBooksThisMonth(countNewBooksThisMonth())
                .allTransactions(countAllTransactions())
                .transactionsThisMonth(countTransactionsThisMonth())
                .allUsers(countAllUsers())
                .newUsersThisMonth(countNewUsersThisMonth())
                .allEvents(countAllEvents())
                .eventsThisMonth(countEventsThisMonth())
                .transactionPercentagesByStatus()
                .newBooksByDate()
                .transactionsByDate()
                .build();
    }

    private long countAllBooks() {
        return 0;
    }

    private long countNewBooksThisMonth() {
        return 0;
    }

    private long countAllTransactions() {
        return 0;
    }

    private long countTransactionsThisMonth() {
        return 0;
    }

    private long countAllUsers() {
        return 0;
    }

    private long countNewUsersThisMonth() {
        return 0;
    }

    private long countAllEvents() {
        return 0;
    }

    private long countEventsThisMonth() {
        return 0;
    }

    private List<PieChartItem> calculateTransactionPercentagesByStatus() {
        return null;
    }

    private List<LineChartItem> countNewBooksByDate() {
        return null;
    }

    private List<LineChartItem> countTransactionsByDate() {
        return null;
    }
}
