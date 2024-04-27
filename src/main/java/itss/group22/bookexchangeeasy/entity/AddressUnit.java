package itss.group22.bookexchangeeasy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

@Entity
@Table(name = "address_unit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressUnit {
    @Id
    @Column(name = "id", length = 20)
    private String id;

    private String name;

    @Column(name = "parent_id", length = 20)
    private String parentId;

    private Integer type;
}
