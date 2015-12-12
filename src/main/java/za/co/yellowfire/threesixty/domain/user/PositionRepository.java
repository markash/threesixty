package za.co.yellowfire.threesixty.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PositionRepository extends MongoRepository<Position, String> {
}
