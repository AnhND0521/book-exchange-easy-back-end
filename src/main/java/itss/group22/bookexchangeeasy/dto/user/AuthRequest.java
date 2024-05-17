package itss.group22.bookexchangeeasy.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthRequest {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
