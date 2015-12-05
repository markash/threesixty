package za.co.yellowfire.threesixty.domain.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionaireService {

	private final RatingQuestionRepository questionRepository;
	private final QuestionaireRepository questionaireRepository;
	
	@Autowired
	public QuestionaireService(
			final RatingQuestionRepository questionRepository,
			final QuestionaireRepository questionaireRepository) {
		
		this.questionRepository = questionRepository;
		this.questionaireRepository = questionaireRepository;
	}
	
	public Questionaire save(final Questionaire questionaire) {
		
		if (questionaire.getQuestions() != null) {
			for (Question<?> question : questionaire.getQuestions()) {
				if (question instanceof RatingQuestion) {
					questionRepository.save((RatingQuestion) question);
				}
			}
		}
		
		return questionaireRepository.save(questionaire);
	}
	
	public void delete(final Questionaire questionaire) {
		this.questionaireRepository.delete(questionaire);
	}
	
	public Questionaire findQuestionaire(final String id) {
		return this.questionaireRepository.findOne(id);
	}
}
