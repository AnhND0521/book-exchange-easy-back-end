package itss.group22.bookexchangeeasy.repository;

import itss.group22.bookexchangeeasy.entity.AddressUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class AddressRepositoryTest {
    @Autowired
    private AddressUnitRepository addressUnitRepository;

    @BeforeEach
    void setUp() {
        AddressUnit hanoi = AddressUnit.builder()
                .id("100001")
                .name("Hà Nội")
                .type(1)
                .build();

        AddressUnit hoChiMinh = AddressUnit.builder()
                .id("100002")
                .name("Hồ Chí Minh")
                .type(1)
                .build();

        AddressUnit haiBaTrung = AddressUnit.builder()
                .id("200001")
                .name("Hai Bà Trưng")
                .type(2)
                .build();

        AddressUnit hoanKiem = AddressUnit.builder()
                .id("200002")
                .name("Hoàn Kiếm")
                .type(2)
                .build();

        AddressUnit dongTam = AddressUnit.builder()
                .id("300001")
                .name("Đồng Tâm")
                .type(3)
                .build();

        AddressUnit truongDinh = AddressUnit.builder()
                .id("300002")
                .name("Trương Định")
                .type(3)
                .build();

        AddressUnit bachKhoa = AddressUnit.builder()
                .id("300003")
                .name("Bách Khoa")
                .type(3)
                .build();

        haiBaTrung.setParent(hanoi);
        hoanKiem.setParent(hanoi);

        dongTam.setParent(haiBaTrung);
        truongDinh.setParent(haiBaTrung);
        bachKhoa.setParent(haiBaTrung);

        addressUnitRepository.saveAll(List.of(hanoi, hoChiMinh, haiBaTrung, hoanKiem, dongTam, truongDinh, bachKhoa));
    }

    @AfterEach
    void tearDown() {
        addressUnitRepository.deleteAll();
    }

    @Test
    void givenTypeProvince_whenFindByType_thenReturnProvinces() {
        List<AddressUnit> provinces = addressUnitRepository.findByTypeOrderByNameAsc(1);
        assertThat(provinces.size()).isEqualTo(2);
        for (var province: provinces) {
            assertThat(province.getType()).isEqualTo(1);
        }
    }

    @Test
    void givenProvinceId_whenFindByParentId_thenReturnDistricts() {
        String provinceId = "100001";
        List<AddressUnit> districts = addressUnitRepository.findByParentIdOrderByNameAsc(provinceId);
        assertThat(districts.size()).isEqualTo(2);
        for (var district: districts) {
            assertThat(district.getType()).isEqualTo(2);
        }
    }

    @Test
    void givenDistrictId_whenFindByParentId_thenReturnCommunes() {
        String districtId = "200001";
        List<AddressUnit> communes = addressUnitRepository.findByParentIdOrderByNameAsc(districtId);
        assertThat(communes.size()).isEqualTo(3);
        for (var commune: communes) {
            assertThat(commune.getType()).isEqualTo(3);
        }
    }
}
