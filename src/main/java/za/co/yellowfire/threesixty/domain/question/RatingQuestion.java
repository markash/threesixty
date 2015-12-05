package za.co.yellowfire.threesixty.domain.question;

import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Auditable;

import com.google.common.collect.Range;
import com.vaadin.server.VaadinSession;

import za.co.yellowfire.threesixty.domain.user.User;

@AccessType(Type.FIELD)
public class RatingQuestion implements RangedQuestion<Integer>, Auditable<User, String> {
	private static final long serialVersionUID = 1L;
	
	@Id
	@ReadOnlyProperty
	private String id;
	private String phrase;
	private Integer answer;
	private Integer upperBound;
	private Integer lowerBound;
	private Integer increment;
	
	@CreatedBy
	private User createdBy;
	@LastModifiedBy
	private User lastModifiedBy;
	@CreatedDate
	private DateTime createdDate;
	@LastModifiedDate
	private DateTime lastModifiedDate;
	@Transient
	private Range<Integer> range;
	
	public RatingQuestion() {}
	
	public RatingQuestion(
			final String id,
			final String phrase,
			final Integer upperBound,
			final Integer lowerBound,
			final Integer increment) {
		this.id = id;
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
	
	public String getId() { return id; }
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
	public boolean isNew() {  return id == null; }
	@Override
	public User getCreatedBy() { return this.createdBy; }
	@Override
	public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
	@Override
	public DateTime getCreatedDate() { return null; }
	@Override
	public void setCreatedDate(DateTime creationDate) { this.createdDate = creationDate; }
	@Override
	public User getLastModifiedBy() { return this.lastModifiedBy; }
	@Override
	public void setLastModifiedBy(User lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }
	@Override
	public DateTime getLastModifiedDate() { return lastModifiedDate; }
	@Override
	public void setLastModifiedDate(DateTime lastModifiedDate) { this.lastModifiedDate = lastModifiedDate; }
	
	public Auditable<User, String> setAuditableInfo(final Auditable<User, String> auditable) {
		User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		
		if (auditable.isNew()) {
			auditable.setCreatedBy(user);
			auditable.setCreatedDate(DateTime.now());
		} else {
			auditable.setLastModifiedBy(user);
			auditable.setLastModifiedDate(DateTime.now());
		}
		
		return auditable;
	}
	
	@Override
	public String toString() {
		return String.format(
				"RatingQuestion [id=%s, phrase=%s, answer=%s, upperBound=%s, lowerBound=%s, increment=%s, createdBy=%s, modifiedBy=%s, created=%s, modified=%s, range=%s]",
				id, phrase, answer, upperBound, lowerBound, increment, createdBy, lastModifiedBy, createdDate, lastModifiedDate, range);
	}
}
