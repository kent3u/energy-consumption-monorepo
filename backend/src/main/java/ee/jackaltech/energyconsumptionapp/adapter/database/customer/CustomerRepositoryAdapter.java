package ee.jackaltech.energyconsumptionapp.adapter.database.customer;

import ee.jackaltech.energyconsumptionapp.adapter.web.security.CustomerDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements UserDetailsService {

    private final CustomerEntityRepository customerEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return customerEntityRepository.getByUsername(username)
                .map(customer -> new CustomerDetails(customer.getId(), customer.getUsername(), customer.getPassword()))
                .orElseThrow();
    }
}
