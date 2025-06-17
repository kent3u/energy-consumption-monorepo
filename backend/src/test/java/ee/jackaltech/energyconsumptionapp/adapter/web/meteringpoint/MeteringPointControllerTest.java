package ee.jackaltech.energyconsumptionapp.adapter.web.meteringpoint;

import ee.jackaltech.energyconsumptionapp.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeteringPointControllerTest extends BaseIntegrationTest {
    @Test
    void findCustomerMeteringPoints_missingSessionId_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/api/metering-point"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("testcustomer")
    @Sql("/testdata/meteringpoint/sql/find_customer_metering_points.sql")
    void findCustomerMeteringPoints_validSession_returnsCustomerMeteringPoints() throws Exception {
        mockMvc.perform(get("/api/metering-point"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.id=='7d4e5f99-8d2d-4637-8a3c-55e59c3af734' && @.address=='Sample address 1')]").exists())
                .andExpect(jsonPath("$[?(@.id=='ab2edd68-acf0-4419-b1ea-36afb17d0f9a' && @.address=='Other address 2')]").exists());
    }
}
