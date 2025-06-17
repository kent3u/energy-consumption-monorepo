package ee.jackaltech.energyconsumptionapp.adapter.web.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

    public static Optional<UUID> getCustomerId() {
        return getCurrentAuthContext().map(CustomerDetails::getCustomerId);
    }

    private static Optional<CustomerDetails> getCurrentAuthContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomerDetails customerDetails) {
            return Optional.of(customerDetails);
        }

        return Optional.empty();
    }
}
