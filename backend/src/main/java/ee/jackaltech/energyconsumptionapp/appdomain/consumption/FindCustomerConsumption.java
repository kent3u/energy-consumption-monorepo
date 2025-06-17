package ee.jackaltech.energyconsumptionapp.appdomain.consumption;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@FunctionalInterface
public interface FindCustomerConsumption {

    Set<Consumption> execute(Request request);

    @Value
    @Builder
    class Request {
        UUID meteringPointId;
        LocalDateTime from;
        LocalDateTime until;
    }
}
