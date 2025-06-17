package ee.jackaltech.energyconsumptionapp.adapter.database.consumption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ConsumptionEntityRepository extends JpaRepository<ConsumptionEntity, UUID>, JpaSpecificationExecutor<ConsumptionEntity> {
}
