package za.co.yellowfire.threesixty.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CountryRepository extends JpaRepository<Country, String>, JpaSpecificationExecutor<Country> {
}
