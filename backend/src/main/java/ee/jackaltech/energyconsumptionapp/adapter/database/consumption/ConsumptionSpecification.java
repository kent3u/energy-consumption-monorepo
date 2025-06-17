package ee.jackaltech.energyconsumptionapp.adapter.database.consumption;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ConsumptionSpecification {

    public Specification<ConsumptionEntity> consumptionTimeBetween(LocalDateTime from, LocalDateTime until) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("consumptionTime"), from, until);
    }

    public Specification<ConsumptionEntity> meteringPointIdEquals(UUID meteringPointId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("meteringPointId"), meteringPointId);
    }
}
