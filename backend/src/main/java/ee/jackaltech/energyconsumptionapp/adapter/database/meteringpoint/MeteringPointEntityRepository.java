package ee.jackaltech.energyconsumptionapp.adapter.database.meteringpoint;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface MeteringPointEntityRepository extends JpaRepository<MeteringPointEntity, UUID> {

    Set<MeteringPointEntity> findByCustomerId(UUID customerId);

    boolean existsByIdAndCustomerId(UUID id, UUID customerId);
}
