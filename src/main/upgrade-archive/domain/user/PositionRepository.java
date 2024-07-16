package za.co.yellowfire.threesixty.domain.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PositionRepository extends JpaRepository<Position, String>, JpaSpecificationExecutor<Position> {
}
