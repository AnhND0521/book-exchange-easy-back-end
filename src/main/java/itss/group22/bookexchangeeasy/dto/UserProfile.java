package itss.group22.bookexchangeeasy.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserProfile {
private String email;
private String name;
private List<String> roles;
private String phoneNumber;
private AddressUnitDTO province;
private AddressUnitDTO district;
private AddressUnitDTO commune;
private String detailedAddress;

}
