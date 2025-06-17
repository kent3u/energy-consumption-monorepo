package ee.jackaltech.energyconsumptionapp.adapter.web.consumption;

import ee.jackaltech.energyconsumptionapp.appdomain.consumption.GetCustomerConsumptionDetails.CustomerConsumptionDetail;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
class ConsumptionDetailsDto {

    int month;
    double kwhAmount;
    double costEur;

    public static ConsumptionDetailsDto of(CustomerConsumptionDetail consumptionDetail) {
        return ConsumptionDetailsDto.builder()
                .month(consumptionDetail.getMonth())
                .kwhAmount(consumptionDetail.getKwhAmount())
                .costEur(consumptionDetail.getCostEur())
                .build();
    }
}
