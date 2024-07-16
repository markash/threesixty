package za.co.yellowfire.threesixty.domain.question;

import jakarta.persistence.Entity;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Transient;

import com.google.common.collect.Range;

import za.co.yellowfire.threesixty.domain.AbstractAuditable;

@Entity
@AccessType(Type.FIELD)
public class RatingQuestion extends AbstractAuditable implements RangedQuestion<Integer> {

	private String phrase;
	private Integer answer;
	private Integer upperBound;
	private Integer lowerBound;
	private Integer increment;

	@Transient
	private Range<Integer> range;
	
	public RatingQuestion() {}
	
	public RatingQuestion(
			final String id,
			final String phrase,
			final Integer upperBound,
			final Integer lowerBound,
			final Integer increment) {
		super(id);
		this.phrase = phrase;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.increment = increment;
		this.range = Range.closed(lowerBound, upperBound);
	}
	
	public static RatingQuestion EMPTY(final RatingQuestionConfiguration configuration) {
		return new RatingQuestion(
				null, 
				"", 
				configuration == null ? 0 : configuration.getUpperBound(), 
				configuration == null ? 0 : configuration.getLowerBound(), 
				configuration == null ? 0 : configuration.getIncrement());
	}

	@Override
	public String getPhrase() { return this.phrase; }
	public void setPhrase(String phrase) { this.phrase = phrase; }
	@Override
	public Integer getAnswer() { return answer; }
	@Override
	public Integer getUpperBound() { return this.upperBound; }
	@Override
	public Integer getLowerBound() { return this.lowerBound; }
	@Override
	public void setUpperBound(final Integer upperBound) { this.upperBound = upperBound; }
	@Override
	public void setLowerBound(final Integer lowerBound) { this.lowerBound = lowerBound; }
	@Override
	public Integer getIncrement() { return this.increment; }
	@Override
	public void setIncrement(final Integer increment) { this.increment = increment; }
	@Override
	public void setAnswer(final Integer answer) throws RatingException { 
		if (!this.range.contains(answer)) {
			throw new RatingException(String.format("The answer %i is not in the range (%i...%i)", answer, lowerBound, upperBound));
		}
	}

	@Override
	public String toString() {
		return String.format(
				"RatingQuestion [id=%s, phrase=%s, answer=%s, upperBound=%s, lowerBound=%s, increment=%s, createdBy=%s, modifiedBy=%s, created=%s, modified=%s, range=%s]",
				getId(), phrase, answer, upperBound, lowerBound, increment, getCreatedBy(), getLastModifiedBy(), getCreatedDate(), getLastModifiedDate(), range);
	}
}
