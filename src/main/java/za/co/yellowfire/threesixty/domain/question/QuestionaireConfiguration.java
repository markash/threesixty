package za.co.yellowfire.threesixty.domain.question;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="questionaire")
public class QuestionaireConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;
}
