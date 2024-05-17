package itss.group22.bookexchangeeasy.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PieChartItem {
    private String proportionName;
    private double percentage;
}
