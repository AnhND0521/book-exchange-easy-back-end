package itss.group22.bookexchangeeasy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "address_unit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private AddressUnit parent;

    @OneToMany(mappedBy = "parent")
    private Set<AddressUnit> children;
}
