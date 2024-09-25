package itss.group22.bookexchangeeasy.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCategoriesRequest {
    private String name;
    private Pageable pageable;
}
