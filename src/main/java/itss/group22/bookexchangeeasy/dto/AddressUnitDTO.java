package itss.group22.bookexchangeeasy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AddressUnitDTO {
    private String id;
    private String name;
}
