package ee.jackaltech.energyconsumptionapp.appdomain.consumption;

import ee.jackaltech.energyconsumptionapp.appdomain.consumption.GetCustomerConsumptionDetails.CustomerConsumptionDetail;
import ee.jackaltech.energyconsumptionapp.appdomain.consumption.QueryHourlyElectricityPricesByDate.ElectricityPrice;
import ee.jackaltech.energyconsumptionapp.appdomain.consumption.exception.MissingElectricityPriceDataException;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.IsCustomerAllowedForMeteringPoint;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.exception.UnauthorizedMeteringPointAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCustomerConsumptionDetailsTest {

    private static final UUID SAMPLE_CUSTOMER_ID = UUID.fromString("d02fce2a-5512-4d67-b115-d06774fcb81c");
    private static final UUID SAMPLE_METERING_POINT_ID = UUID.fromString("bf373d3d-2b5b-4500-9068-5847ade7361b");
    private static final LocalDateTime SAMPLE_FROM_DATE_TIME = LocalDateTime.of(2024, 1, 1, 0, 0);
    private static final LocalDateTime SAMPLE_UNTIL_DATE_TIME = LocalDateTime.of(2024, 12, 31, 23, 59);
    private static final UUID SAMPLE_CONSUMPTION_ID = UUID.fromString("d16e0750-6132-4559-bc4d-74693963979e");

    @Mock
    private QueryHourlyElectricityPricesByDate queryHourlyElectricityPricesByDate;
    @Mock
    private IsCustomerAllowedForMeteringPoint isCustomerAllowedForMeteringPoint;
    @Mock
    private FindCustomerConsumption findCustomerConsumption;

    private GetCustomerConsumptionDetails getCustomerConsumptionDetails;

    @BeforeEach
    void setup() {
        getCustomerConsumptionDetails = new GetCustomerConsumptionDetails(queryHourlyElectricityPricesByDate,
                isCustomerAllowedForMeteringPoint,
                findCustomerConsumption);
    }

    @Test
    void execute_validData_returnsConsumptionDetails() {
        when(isCustomerAllowedForMeteringPoint.execute(IsCustomerAllowedForMeteringPoint.Request.of(SAMPLE_CUSTOMER_ID, SAMPLE_METERING_POINT_ID)))
                .thenReturn(true);

        mockElectricityPrices();
        Consumption consumption = Consumption.builder()
                .id(SAMPLE_CONSUMPTION_ID)
                .consumptionTime(LocalDateTime.of(2024, 4, 12, 10, 0))
                .kwhAmount(BigDecimal.valueOf(123))
                .build();
        Consumption consumption1 = Consumption.builder()
                .id(UUID.randomUUID())
                .consumptionTime(LocalDateTime.of(2024, 4, 12, 20, 0))
                .kwhAmount(BigDecimal.valueOf(1))
                .build();
        mockCustomerConsumption(Set.of(consumption, consumption1));

        Set<CustomerConsumptionDetail> result = getCustomerConsumptionDetails.execute(composeSampleRequest().build());

        assertThat(result)
                .hasSize(12)
                .anySatisfy(consumptionDetail -> {
                    assertThat(consumptionDetail.getMonth()).isEqualTo(4);
                    assertThat(consumptionDetail.getKwhAmount()).isEqualTo(124);
                    assertThat(consumptionDetail.getCostEur()).isEqualTo(45.08901);
                })
                .allSatisfy(detail -> {
                    if (detail.getMonth() != 4) {
                        assertThat(detail.getKwhAmount()).isZero();
                        assertThat(detail.getCostEur()).isZero();
                    }
                });
    }

    @Test
    void execute_customerDoesntOwnMeteringPoint_throwsUnauthorizedMeteringPointAccessException() {
        when(isCustomerAllowedForMeteringPoint.execute(IsCustomerAllowedForMeteringPoint.Request.of(SAMPLE_CUSTOMER_ID, SAMPLE_METERING_POINT_ID)))
                .thenReturn(false);

        assertThrows(UnauthorizedMeteringPointAccessException.class, () -> getCustomerConsumptionDetails.execute(composeSampleRequest().build()));
    }

    @Test
    void execute_priceForConsumptionMissing_throwsMissingElectricityPriceDataException() {
        when(isCustomerAllowedForMeteringPoint.execute(IsCustomerAllowedForMeteringPoint.Request.of(SAMPLE_CUSTOMER_ID, SAMPLE_METERING_POINT_ID)))
                .thenReturn(true);

        mockElectricityPrices();
        Consumption consumption = Consumption.builder()
                .id(SAMPLE_CONSUMPTION_ID)
                .consumptionTime(LocalDateTime.of(2024, 1, 12, 10, 0))
                .kwhAmount(BigDecimal.valueOf(123))
                .build();
        mockCustomerConsumption(Set.of(consumption));

        assertThrows(MissingElectricityPriceDataException.class, () -> getCustomerConsumptionDetails.execute(composeSampleRequest().build()));
    }

    private void mockCustomerConsumption(Set<Consumption> consumptions) {
        when(findCustomerConsumption.execute(FindCustomerConsumption.Request.builder()
                        .meteringPointId(SAMPLE_METERING_POINT_ID)
                        .from(SAMPLE_FROM_DATE_TIME)
                        .until(SAMPLE_UNTIL_DATE_TIME)
                .build())).thenReturn(consumptions);
    }

    private void mockElectricityPrices() {
        Map<Integer, ElectricityPrice> hourlyPrices = new HashMap<>();
        hourlyPrices.put(10, ElectricityPrice.of(BigDecimal.valueOf(35.75432)));
        hourlyPrices.put(11, ElectricityPrice.of(BigDecimal.valueOf(5.09901)));
        hourlyPrices.put(20, ElectricityPrice.of(BigDecimal.valueOf(111.12)));

        Map<LocalDate, Map<Integer, ElectricityPrice>> priceByDateAndHour = new HashMap<>();
        priceByDateAndHour.put(LocalDate.of(2024, 4, 12), hourlyPrices);

        when(queryHourlyElectricityPricesByDate.execute(QueryHourlyElectricityPricesByDate.Request.builder()
                        .from(SAMPLE_FROM_DATE_TIME)
                        .until(SAMPLE_UNTIL_DATE_TIME)
                .build())).thenReturn(priceByDateAndHour);
    }

    private static GetCustomerConsumptionDetails.Request.RequestBuilder composeSampleRequest() {
        return GetCustomerConsumptionDetails.Request.builder()
                .customerId(SAMPLE_CUSTOMER_ID)
                .meteringPointId(SAMPLE_METERING_POINT_ID)
                .from(SAMPLE_FROM_DATE_TIME)
                .until(SAMPLE_UNTIL_DATE_TIME);
    }
}
