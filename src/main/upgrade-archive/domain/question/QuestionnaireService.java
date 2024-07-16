package za.co.yellowfire.threesixty.domain.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionnaireService {

	private final RatingQuestionRepository questionRepository;
	private final QuestionnaireRepository questionnaireRepository;
	
	@Autowired
	public QuestionnaireService(
			final RatingQuestionRepository questionRepository,
			final QuestionnaireRepository questionnaireRepository) {
		
		this.questionRepository = questionRepository;
		this.questionnaireRepository = questionnaireRepository;
	}
	
	public Questionnaire save(final Questionnaire questionnaire) {
		
		if (questionnaire.getQuestions() != null) {
			for (Question<?> question : questionnaire.getQuestions()) {
				if (question instanceof RatingQuestion) {
					questionRepository.save((RatingQuestion) question);
				}
			}
		}
		
		return questionnaireRepository.save(questionnaire);
	}
	
	public void delete(final Questionnaire questionnaire) {
		this.questionnaireRepository.delete(questionnaire);
	}
	
	public Optional<Questionnaire> findQuestionnaire(final String id) {
		return this.questionnaireRepository.findOne(Example.of(Questionnaire.ID(id)));
	}
}
