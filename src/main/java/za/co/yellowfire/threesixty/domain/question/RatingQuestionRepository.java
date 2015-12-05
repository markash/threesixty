package za.co.yellowfire.threesixty.domain.question;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RatingQuestionRepository extends MongoRepository<RatingQuestion, String> {
}
