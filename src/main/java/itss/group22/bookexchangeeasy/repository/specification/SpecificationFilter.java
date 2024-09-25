package itss.group22.bookexchangeeasy.repository.specification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SpecificationFilter {
    private String field;
    private QueryOperator operator;
    private Object value;
    private List<?> values;
}
