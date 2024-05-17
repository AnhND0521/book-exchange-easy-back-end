package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.user.AddressUnitDTO;

import java.util.List;

public interface AddressService {
    List<AddressUnitDTO> getProvinces();
    List<AddressUnitDTO> getDistrictsByProvinceId(String provinceId);
    List<AddressUnitDTO> getCommunesByDistrictId(String districtId);
}
