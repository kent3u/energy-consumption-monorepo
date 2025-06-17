package ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint;

import lombok.Value;

import java.util.UUID;

@FunctionalInterface
public interface IsCustomerAllowedForMeteringPoint {

    boolean execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        UUID customerId;
        UUID meteringPointId;
    }
}
