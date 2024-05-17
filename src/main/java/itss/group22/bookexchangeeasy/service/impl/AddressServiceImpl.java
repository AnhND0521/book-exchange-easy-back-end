package itss.group22.bookexchangeeasy.service.impl;

import itss.group22.bookexchangeeasy.dto.user.AddressUnitDTO;
import itss.group22.bookexchangeeasy.entity.AddressUnit;
import itss.group22.bookexchangeeasy.exception.ApiException;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.AddressUnitRepository;
import itss.group22.bookexchangeeasy.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressUnitRepository addressUnitRepository;
    private final ModelMapper mapper;

    @Override
    public List<AddressUnitDTO> getProvinces() {
        return addressUnitRepository.findByTypeOrderByNameAsc(1).stream()
                .map(addressUnit -> mapper.map(addressUnit, AddressUnitDTO.class))
                .toList();
    }

    @Override
    public List<AddressUnitDTO> getDistrictsByProvinceId(String provinceId) {
        AddressUnit province = addressUnitRepository.findById(provinceId)
                .orElseThrow(() -> new ResourceNotFoundException("address unit", "id", provinceId));

        if (!province.getType().equals(1))
            throw new ApiException("Provided id does not belong to a province");

        return addressUnitRepository.findByParentIdOrderByNameAsc(provinceId).stream()
                .map(addressUnit -> mapper.map(addressUnit, AddressUnitDTO.class))
                .toList();
    }

    @Override
    public List<AddressUnitDTO> getCommunesByDistrictId(String districtId) {
        AddressUnit district = addressUnitRepository.findById(districtId)
                .orElseThrow(() -> new ResourceNotFoundException("address unit", "id", districtId));

        if (!district.getType().equals(2))
            throw new ApiException("Provided id does not belong to a district");

        return addressUnitRepository.findByParentIdOrderByNameAsc(districtId).stream()
                .map(addressUnit -> mapper.map(addressUnit, AddressUnitDTO.class))
                .toList();
    }
}
