package itss.group22.bookexchangeeasy.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ChangePasswordDTO {
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    @Size(min = 8)
    private String newPassword;

}
