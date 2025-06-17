package ee.jackaltech.energyconsumptionapp.adapter.database.consumption;

import ee.jackaltech.energyconsumptionapp.appdomain.consumption.Consumption;
import ee.jackaltech.energyconsumptionapp.appdomain.consumption.FindCustomerConsumption;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConsumptionRepositoryAdapter implements FindCustomerConsumption {

    private final ConsumptionSpecification consumptionSpecification;
    private final ConsumptionEntityRepository consumptionEntityRepository;

    @Override
    public Set<Consumption> execute(Request request) {
        Specification<ConsumptionEntity> spec = consumptionSpecification.meteringPointIdEquals(request.getMeteringPointId())
                .and(consumptionSpecification.consumptionTimeBetween(request.getFrom(), request.getUntil()));
        return consumptionEntityRepository.findAll(spec)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableSet());
    }

    private Consumption toDomain(ConsumptionEntity consumptionEntity) {
        return Consumption.builder()
                .id(consumptionEntity.getId())
                .meteringPointId(consumptionEntity.getMeteringPointId())
                .kwhAmount(consumptionEntity.getAmount())
                .consumptionTime(consumptionEntity.getConsumptionTime())
                .build();
    }
}
