package itss.group22.bookexchangeeasy.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverviewStatistics {
    private long allBooks;
    private long newBooksThisMonth;
    private long allTransactions;
    private long transactionsThisMonth;
    private long allUsers;
    private long newUsersThisMonth;
    private long allEvents;
    private long eventsThisMonth;
}