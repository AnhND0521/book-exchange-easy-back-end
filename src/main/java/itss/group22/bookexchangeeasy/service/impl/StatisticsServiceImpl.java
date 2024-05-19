package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.statistics.LineChartItem;
import itss.group22.bookexchangeeasy.dto.statistics.OverviewStatistics;
import itss.group22.bookexchangeeasy.dto.statistics.PieChartItem;
import itss.group22.bookexchangeeasy.enums.TransactionStatus;
import itss.group22.bookexchangeeasy.repository.BookRepository;
import itss.group22.bookexchangeeasy.repository.StoreEventRepository;
import itss.group22.bookexchangeeasy.repository.TransactionRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import itss.group22.bookexchangeeasy.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final StoreEventRepository storeEventRepository;

    @Override
    public OverviewStatistics getOverviewStatistics() {
        return OverviewStatistics.builder()
                .allBooks(allBooks())
                .newBooksThisMonth(newBooksThisMonth())
                .allTransactions(allTransactions())
                .transactionsThisMonth(transactionsThisMonth())
                .allUsers(allUsers())
                .newUsersThisMonth(newUsersThisMonth())
                .allEvents(allEvents())
                .eventsThisMonth(eventsThisMonth())
                .build();
    }

    Long allBooks() {
        return bookRepository.count();
    }

    Long newBooksThisMonth() {
        LocalDate today = LocalDate.now();
        return bookRepository.countByMonth(today.getYear(), today.getMonthValue());
    }

    Long allTransactions() {
        return transactionRepository.count();
    }

    Long transactionsThisMonth() {
        LocalDate today = LocalDate.now();
        return transactionRepository.countByMonth(today.getYear(), today.getMonthValue());
    }

    Long allUsers() {
        return userRepository.count();
    }

    Long newUsersThisMonth() {
        LocalDate today = LocalDate.now();
        return userRepository.countByMonth(today.getYear(), today.getMonthValue());
    }

    Long allEvents() {
        return storeEventRepository.count();
    }

    Long eventsThisMonth() {
        LocalDate today = LocalDate.now();
        return storeEventRepository.countByMonth(today.getYear(), today.getMonthValue());
    }

    @Override
    public List<PieChartItem> getTransactionPercentagesByStatus(String filterBy) {
        long allTransactions = transactionRepository.count();
        return Arrays.stream(TransactionStatus.values())
                .map(status -> {
                    long countByStatus = transactionRepository.countByStatus(status);
                    double percentage = 100.0 * countByStatus / allTransactions;
                    return new PieChartItem(status.name(), percentage);
                }).toList();
    }

    @Override
    public List<LineChartItem> getNewBookCountByDate(LocalDate from, LocalDate to) {
        if (to == null) to = LocalDate.now();
        if (from == null) from = to.minusDays(6);

        List<LineChartItem> items = new ArrayList<>();

        var date = from;
        while (true) {
            long count = bookRepository.countByDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            items.add(new LineChartItem(date.toString(), count));

            if (date.isEqual(to)) break;
            date = date.plusDays(1);
        }

        return items;
    }

    @Override
    public List<LineChartItem> getTransactionCountByDate(LocalDate from, LocalDate to) {
        if (to == null) to = LocalDate.now();
        if (from == null) from = to.minusDays(6);

        List<LineChartItem> items = new ArrayList<>();

        var date = from;
        while (true) {
            long count = transactionRepository.countByDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            items.add(new LineChartItem(date.toString(), count));

            if (date.isEqual(to)) break;
            date = date.plusDays(1);
        }

        return items;
    }
}
