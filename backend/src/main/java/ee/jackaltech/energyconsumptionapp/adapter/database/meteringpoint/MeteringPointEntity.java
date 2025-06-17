package ee.jackaltech.energyconsumptionapp.adapter.database.meteringpoint;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "metering_point")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MeteringPointEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    @Column(name = "customer_id")
    private UUID customerId;
    @Column(name = "address")
    private String address;
}
