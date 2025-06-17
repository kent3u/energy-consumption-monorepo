package ee.jackaltech.energyconsumptionapp.appdomain.consumption;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class Consumption {

    UUID id;
    UUID meteringPointId;
    BigDecimal kwhAmount;
    LocalDateTime consumptionTime;
}
