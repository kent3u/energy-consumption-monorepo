package ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class MeteringPoint {

    UUID id;
    String address;
}
