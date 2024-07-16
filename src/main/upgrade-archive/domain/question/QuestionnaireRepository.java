package za.co.yellowfire.threesixty.domain.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, String>, JpaSpecificationExecutor<Questionnaire> {
}
