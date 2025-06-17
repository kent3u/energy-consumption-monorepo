package ee.jackaltech.energyconsumptionapp.adapter.web.consumption;

import ee.jackaltech.energyconsumptionapp.adapter.web.security.SecurityUtil;
import ee.jackaltech.energyconsumptionapp.appdomain.consumption.GetCustomerConsumptionDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumption")
class ConsumptionController {

    private final GetCustomerConsumptionDetails getCustomerConsumptionDetails;

    @GetMapping
    public Set<ConsumptionDetailsDto> getCustomerConsumptionDetails(@RequestParam UUID meteringPointId,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime until) {
        UUID customerId = SecurityUtil.getCustomerId().orElseThrow();
        return getCustomerConsumptionDetails.execute(GetCustomerConsumptionDetails.Request.builder()
                        .customerId(customerId)
                        .meteringPointId(meteringPointId)
                        .from(from)
                        .until(until)
                        .build())
                .stream()
                .map(ConsumptionDetailsDto::of)
                .collect(Collectors.toUnmodifiableSet());
    }
}
