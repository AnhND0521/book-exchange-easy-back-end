package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.statistics.LineChartItem;
import itss.group22.bookexchangeeasy.dto.statistics.OverviewStatistics;
import itss.group22.bookexchangeeasy.dto.statistics.PieChartItem;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {
    OverviewStatistics getOverviewStatistics();

    List<PieChartItem> getTransactionPercentagesByStatus(String filterBy);

    List<LineChartItem> getNewBookCountByDate(LocalDate from, LocalDate to);

    List<LineChartItem> getTransactionCountByDate(LocalDate from, LocalDate to);
}
