package ee.jackaltech.energyconsumptionapp.appdomain.consumption;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@FunctionalInterface
public interface QueryHourlyElectricityPricesByDate {

    Map<LocalDate, Map<Integer, ElectricityPrice>> execute(Request request);

    @Value
    @Builder
    class Request {
        LocalDateTime from;
        LocalDateTime until;
    }

    @Value(staticConstructor = "of")
    class ElectricityPrice {
        BigDecimal centsPerKwhWithVat;
    }
}
