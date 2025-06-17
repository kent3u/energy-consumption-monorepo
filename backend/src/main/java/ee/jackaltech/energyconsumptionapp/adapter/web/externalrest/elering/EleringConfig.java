package ee.jackaltech.energyconsumptionapp.adapter.web.externalrest.elering;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class EleringConfig {

    private final String baseEleringUrl;

    public EleringConfig(@Value("${elering.rest.url}") String baseEleringUrl) {
        this.baseEleringUrl = baseEleringUrl;
    }

    @Bean
    public RestClient eleringRestClient() {
        return RestClient.builder()
                .baseUrl(baseEleringUrl)
                .build();
    }
}
