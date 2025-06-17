package ee.jackaltech.energyconsumptionapp.adapter.web.meteringpoint;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
class MeteringPointDto {

    UUID id;
    String address;
}
