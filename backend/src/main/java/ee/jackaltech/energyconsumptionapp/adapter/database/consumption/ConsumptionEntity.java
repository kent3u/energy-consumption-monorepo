package ee.jackaltech.energyconsumptionapp.adapter.database.consumption;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumption")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ConsumptionEntity {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    @Column(name = "metering_point_id")
    private UUID meteringPointId;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "consumption_time")
    private LocalDateTime consumptionTime;
}
