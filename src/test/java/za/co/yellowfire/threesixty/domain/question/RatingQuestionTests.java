package za.co.yellowfire.threesixty.domain.question;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import za.co.yellowfire.threesixty.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class RatingQuestionTests {

	@Autowired
	RatingQuestionRepository repository;
	
	@Test
	public void testPersistence() {
		
		repository.deleteAll();
		
		RatingQuestion question = 
				new RatingQuestion(
						"Love of ice-cream",
						"How much to you love ice-cream",
						5, 1, 1);
		
		RatingQuestion persisted = repository.save(question);
		assertNotNull(persisted);
		System.out.println(persisted);
		
		persisted = repository.findOne("Love of ice-cream");
		
		System.out.println(persisted.isNew());
	}
}
