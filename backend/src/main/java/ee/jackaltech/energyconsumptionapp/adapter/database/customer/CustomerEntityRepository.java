package ee.jackaltech.energyconsumptionapp.adapter.database.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface CustomerEntityRepository extends JpaRepository<CustomerEntity, UUID> {

    Optional<CustomerEntity> getByUsername(String username);
}
