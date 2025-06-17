package ee.jackaltech.energyconsumptionapp.adapter.web.meteringpoint;

import ee.jackaltech.energyconsumptionapp.adapter.web.security.SecurityUtil;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.FindCustomerMeteringPoints;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metering-point")
@RequiredArgsConstructor
class MeteringPointController {

    private final FindCustomerMeteringPoints findCustomerMeteringPoints;

    @GetMapping
    public Set<MeteringPointDto> findCustomerMeteringPoints() {
        UUID customerId = SecurityUtil.getCustomerId().orElseThrow();
        return findCustomerMeteringPoints.execute(FindCustomerMeteringPoints.Request.of(customerId)).stream()
                .map(meteringPoint -> MeteringPointDto.builder()
                        .id(meteringPoint.getId())
                        .address(meteringPoint.getAddress())
                        .build())
                .collect(Collectors.toUnmodifiableSet());
    }
}
