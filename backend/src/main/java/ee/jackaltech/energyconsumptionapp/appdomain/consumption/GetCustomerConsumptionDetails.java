package ee.jackaltech.energyconsumptionapp.appdomain.consumption;

import ee.jackaltech.energyconsumptionapp.appdomain.consumption.QueryHourlyElectricityPricesByDate.ElectricityPrice;
import ee.jackaltech.energyconsumptionapp.appdomain.consumption.exception.MissingElectricityPriceDataException;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.IsCustomerAllowedForMeteringPoint;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.exception.UnauthorizedMeteringPointAccessException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class GetCustomerConsumptionDetails {

    private final QueryHourlyElectricityPricesByDate queryHourlyElectricityPricesByDate;
    private final IsCustomerAllowedForMeteringPoint isCustomerAllowedForMeteringPoint;
    private final FindCustomerConsumption findCustomerConsumption;

    public Set<CustomerConsumptionDetail> execute(Request request) {
        if (!isCustomerAllowedForMeteringPoint.execute(IsCustomerAllowedForMeteringPoint.Request.of(request.getCustomerId(), request.getMeteringPointId()))) {
            throw new UnauthorizedMeteringPointAccessException();
        }

        Map<LocalDate, Map<Integer, ElectricityPrice>> priceByDateAndHour =
                queryHourlyElectricityPricesByDate.execute(QueryHourlyElectricityPricesByDate.Request.builder()
                        .from(request.getFrom())
                        .until(request.getUntil())
                        .build());

        Map<Integer, Set<Consumption>> consumptionsByMonth = findCustomerConsumption.execute(FindCustomerConsumption.Request.builder()
                        .meteringPointId(request.getMeteringPointId())
                        .from(request.getFrom())
                        .until(request.getUntil())
                        .build())
                .stream()
                .collect(Collectors.groupingBy(c -> c.getConsumptionTime().getMonthValue(), Collectors.toSet()));


        return IntStream.rangeClosed(1, 12)
                .mapToObj(month -> {
                    Set<Consumption> monthConsumptions = consumptionsByMonth.getOrDefault(month, Set.of());
                    return aggregateMonthlyConsumption(month, monthConsumptions, priceByDateAndHour);
                })
                .collect(Collectors.toUnmodifiableSet());
    }

    private CustomerConsumptionDetail aggregateMonthlyConsumption(int month,
                                                                  Set<Consumption> monthConsumptions,
                                                                  Map<LocalDate, Map<Integer, ElectricityPrice>> priceByDateAndHour) {
        BigDecimal totalKwh = BigDecimal.ZERO;
        BigDecimal totalCostCents = BigDecimal.ZERO;

        for (Consumption consumption : monthConsumptions) {
            Map<Integer, ElectricityPrice> hourlyPrices = priceByDateAndHour.get(consumption.getConsumptionTime().toLocalDate());
            if (hourlyPrices == null || hourlyPrices.get(consumption.getConsumptionTime().getHour()) == null) {
                throw new MissingElectricityPriceDataException();
            }
            // todo handle negative prices according to business logic
            BigDecimal consumptionCostWithVat = hourlyPrices.get(consumption.getConsumptionTime().getHour()).getCentsPerKwhWithVat();

            totalKwh = totalKwh.add(consumption.getKwhAmount());
            totalCostCents = totalCostCents.add(consumptionCostWithVat.multiply(consumption.getKwhAmount()));
        }

        return CustomerConsumptionDetail.builder()
                .month(month)
                .kwhAmount(totalKwh.setScale(5, RoundingMode.HALF_UP).doubleValue())
                .costEur(totalCostCents.divide(BigDecimal.valueOf(100), 5, RoundingMode.HALF_UP).doubleValue())
                .build();
    }

    @Value
    @Builder
    public static class Request {
        LocalDateTime from;
        LocalDateTime until;
        UUID customerId;
        UUID meteringPointId;
    }

    @Value
    @Builder
    public static class CustomerConsumptionDetail {
        int month;
        double kwhAmount;
        double costEur;
    }
}
