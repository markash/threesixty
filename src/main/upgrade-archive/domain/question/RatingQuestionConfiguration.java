package za.co.yellowfire.threesixty.domain.question;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="rating.question")
public class RatingQuestionConfiguration {
	//rating.question.lowerBound=1
	private int lowerBound;
	
	//rating.question.upperBound=5
	private int upperBound;
	
	//rating.question.increment=1
	private int increment;
	
	//rating.question.locked=true
	private boolean locked;

	public int getLowerBound() { return lowerBound; }
	public void setLowerBound(int lowerBound) { this.lowerBound = lowerBound; }

	public int getUpperBound() { return upperBound; }
	public void setUpperBound(int upperBound) { this.upperBound = upperBound; }

	public int getIncrement() { return increment; }
	public void setIncrement(int increment) { this.increment = increment; }

	public boolean isLocked() { return locked; }
	public void setLocked(boolean locked) { this.locked = locked; }
}
