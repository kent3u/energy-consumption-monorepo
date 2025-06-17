package ee.jackaltech.energyconsumptionapp.adapter.database.meteringpoint;

import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.FindCustomerMeteringPoints;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.IsCustomerAllowedForMeteringPoint;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.MeteringPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MeteringPointRepositoryAdapter implements FindCustomerMeteringPoints, IsCustomerAllowedForMeteringPoint {

    private final MeteringPointEntityRepository meteringPointEntityRepository;

    @Override
    public Set<MeteringPoint> execute(FindCustomerMeteringPoints.Request request) {
        return meteringPointEntityRepository.findByCustomerId(request.getCustomerId()).stream()
                .map(this::toDomain)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean execute(IsCustomerAllowedForMeteringPoint.Request request) {
        return meteringPointEntityRepository.existsByIdAndCustomerId(request.getMeteringPointId(), request.getCustomerId());
    }

    private MeteringPoint toDomain(MeteringPointEntity meteringPointEntity) {
        return MeteringPoint.builder()
                .id(meteringPointEntity.getId())
                .address(meteringPointEntity.getAddress())
                .build();
    }
}
