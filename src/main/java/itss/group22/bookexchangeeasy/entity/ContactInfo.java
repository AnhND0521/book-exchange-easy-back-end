package itss.group22.bookexchangeeasy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "contact_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private AddressUnit province;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private AddressUnit district;

    @ManyToOne
    @JoinColumn(name = "commune_id")
    private AddressUnit commune;

    private String detailedAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactInfo that = (ContactInfo) o;

        if (!Objects.equals(phoneNumber, that.phoneNumber)) return false;
        if (!Objects.equals(province.getId(), that.province.getId())) return false;
        if (!Objects.equals(district.getId(), that.district.getId())) return false;
        if (!Objects.equals(commune.getId(), that.commune.getId())) return false;
        return Objects.equals(detailedAddress, that.detailedAddress);
    }
}
