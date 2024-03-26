package itss.group22.bookexchangeeasy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contact_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "province_id")
    private AddressUnit province;

    @OneToOne
    @JoinColumn(name = "district_id")
    private AddressUnit district;

    @OneToOne
    @JoinColumn(name = "commune_id")
    private AddressUnit commune;

    private String detailedAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
