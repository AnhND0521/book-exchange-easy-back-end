package itss.group22.bookexchangeeasy.dto;

import java.time.LocalDate;
import java.util.List;

import itss.group22.bookexchangeeasy.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    private String email;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private List<String> roles;
    private String phoneNumber;
    private AddressUnitDTO province;
    private AddressUnitDTO district;
    private AddressUnitDTO commune;
    private String detailedAddress;
}
