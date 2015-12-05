package za.co.yellowfire.threesixty.domain.question;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionaireRepository extends MongoRepository<Questionaire, String> {
}
