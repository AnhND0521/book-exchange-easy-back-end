package itss.group22.bookexchangeeasy.dto.user;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    private Long id;
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
    private String pictureUrl;

    public boolean hasContactInfo() {
        return phoneNumber != null || province != null || district != null || commune != null || detailedAddress != null;
    }
}
