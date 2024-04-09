package itss.group22.bookexchangeeasy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ImageDTO {
    private Long id;
    private String path;
    private String caption;
}
