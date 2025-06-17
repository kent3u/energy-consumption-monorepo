package ee.jackaltech.energyconsumptionapp.adapter.web.externalrest.elering;

import ee.jackaltech.energyconsumptionapp.appdomain.consumption.QueryHourlyElectricityPricesByDate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class EleringAdapter implements QueryHourlyElectricityPricesByDate {

    private final RestClient eleringRestClient;

    @Override
    public Map<LocalDate, Map<Integer, ElectricityPrice>> execute(Request request) {
        List<ElectricityPriceDto> response = eleringRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/public/v1/energy-price/electricity")
                        .queryParam("startDateTime", request.getFrom().atOffset(ZoneOffset.UTC))
                        .queryParam("endDateTime", request.getUntil().atOffset(ZoneOffset.UTC))
                        .queryParam("resolution", "one_hour")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<ElectricityPriceDto>>() {
                });

        return response.stream()
                .collect(Collectors.groupingBy(
                        dto -> dto.getFromDateTime().toLocalDate(),
                        Collectors.toMap(dto -> dto.getFromDateTime().getHour(), this::toDomain)
                ));
    }

    private ElectricityPrice toDomain(ElectricityPriceDto electricityPriceDto) {
        return ElectricityPrice.of(electricityPriceDto.getCentsPerKwhWithVat());
    }

    @Data
    private static class ElectricityPriceDto {
        private BigDecimal centsPerKwhWithVat;
        private OffsetDateTime fromDateTime;
    }
}
