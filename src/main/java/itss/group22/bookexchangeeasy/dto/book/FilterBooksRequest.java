package itss.group22.bookexchangeeasy.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterBooksRequest {
    private String title;
    private String author;
    private String publisher;
    private Long ownerId;
    private Long categoryId;
    private Pageable pageable;
}
