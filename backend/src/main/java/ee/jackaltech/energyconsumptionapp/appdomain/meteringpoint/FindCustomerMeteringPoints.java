package ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint;

import lombok.Value;

import java.util.Set;
import java.util.UUID;

@FunctionalInterface
public interface FindCustomerMeteringPoints {

    Set<MeteringPoint> execute(Request request);

    @Value(staticConstructor = "of")
    class Request {
        UUID customerId;
    }
}
