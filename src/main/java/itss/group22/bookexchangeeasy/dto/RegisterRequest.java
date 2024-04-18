package itss.group22.bookexchangeeasy.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotEmpty @Email
    String email;

    @NotEmpty @Size(min = 8)
    String password;

    @NotEmpty
    List<String> roles;

    @NotEmpty
    String name;

    @NotEmpty
    String gender;

    LocalDate birthDate;

    @Pattern(regexp = "[0-9]{8,12}")
    String phoneNumber;

    String provinceId;
    String districtId;
    String communeId;
    String detailedAddress;
}
