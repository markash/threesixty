package za.co.yellowfire.threesixty.domain.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ObjectiveRepository extends JpaRepository<Objective, String>, JpaSpecificationExecutor<Objective> {
}
