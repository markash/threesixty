package za.co.yellowfire.threesixty.domain.rating;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OutcomeRepository extends MongoRepository<Outcome, String> {
}
