package ee.jackaltech.energyconsumptionapp.adapter.database.meteringpoint;

import ee.jackaltech.energyconsumptionapp.BaseIntegrationTest;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.FindCustomerMeteringPoints;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.IsCustomerAllowedForMeteringPoint;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.MeteringPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MeteringPointRepositoryAdapterTest extends BaseIntegrationTest {

    private static final UUID SAMPLE_CUSTOMER_ID = UUID.fromString("09483222-673c-4bc6-927c-dbcd8b8026d6");
    private static final UUID SAMPLE_UUID = UUID.fromString("dca1262d-bc39-4dfa-8fc1-9c5e9811a54a");
    private static final String SAMPLE_ADDRESS = "Oak ave 123";


    @Autowired
    private MeteringPointEntityRepository meteringPointEntityRepository;

    @Autowired
    private MeteringPointRepositoryAdapter adapter;

    @BeforeEach
    void clearState() {
        meteringPointEntityRepository.deleteAll();
    }

    @Test
    @Sql("/testdata/meteringpoint/sql/insert_customers_for_metering_points.sql")
    void findCustomerMeteringPoints_validData_returnsMeteringPoints() {
        MeteringPointEntity meteringPoint = composeMeteringPointEntityBuilder().build();
        UUID secondId = UUID.fromString("96ac16d1-af4b-40a7-831c-63bc25d1e0c6");
        MeteringPointEntity meteringPoint1 = composeMeteringPointEntityBuilder()
                .address("Birch st 456")
                .id(secondId)
                .build();
        meteringPointEntityRepository.saveAll(Set.of(meteringPoint, meteringPoint1));

        Set<MeteringPoint> result = adapter.execute(FindCustomerMeteringPoints.Request.of(SAMPLE_CUSTOMER_ID));

        assertThat(result).hasSize(2)
                .anySatisfy(point -> {
                    assertThat(point.getId()).isEqualTo(SAMPLE_UUID);
                    assertThat(point.getAddress()).isEqualTo(SAMPLE_ADDRESS);
                })
                .anySatisfy(point -> {
                    assertThat(point.getId()).isEqualTo(secondId);
                    assertThat(point.getAddress()).isEqualTo("Birch st 456");
                });
    }

    @Test
    @Sql("/testdata/meteringpoint/sql/insert_customers_for_metering_points.sql")
    void isCustomerAllowedForMeteringPoint_validData_returnTrue() {
        meteringPointEntityRepository.save(composeMeteringPointEntityBuilder().build());

        boolean result = adapter.execute(IsCustomerAllowedForMeteringPoint.Request.of(SAMPLE_CUSTOMER_ID, SAMPLE_UUID));

        assertThat(result).isTrue();
    }

    @Test
    @Sql("/testdata/meteringpoint/sql/insert_customers_for_metering_points.sql")
    void isCustomerAllowedForMeteringPoint_customerDoesntOwnMeteringPoint_returnFalse() {
        meteringPointEntityRepository.save(composeMeteringPointEntityBuilder().build());

        boolean result = adapter.execute(IsCustomerAllowedForMeteringPoint.Request.of(UUID.randomUUID(), SAMPLE_UUID));

        assertThat(result).isFalse();
    }

    private MeteringPointEntity.MeteringPointEntityBuilder composeMeteringPointEntityBuilder() {
        return MeteringPointEntity.builder()
                .id(SAMPLE_UUID)
                .customerId(SAMPLE_CUSTOMER_ID)
                .address(SAMPLE_ADDRESS);
    }
}
