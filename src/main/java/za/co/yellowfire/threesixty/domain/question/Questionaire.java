package za.co.yellowfire.threesixty.domain.question;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Auditable;

import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.domain.user.User;

/**
 * A set of questions
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@AccessType(Type.FIELD)
public class Questionaire implements Auditable<User, String> {
	private static final long serialVersionUID = 1L;
	
	@Id @ReadOnlyProperty
	private String id;
	
	@NotNull
	private String name;
	private Period period; 
	private Set<Question<?>> questions;
	
	public Questionaire() {
		this(null, null, (Set<Question<?>>) null);
	}
	
	public Questionaire(final String name) {
		this(name, null, (Set<Question<?>>) null);
	}
	
	public Questionaire(String name, Period period, Set<Question<?>> questions) {
		super();
		this.name = name;
		this.period = period == null ? new Period() : period;
		this.questions = questions == null ? new HashSet<Question<?>>() : questions;
	}

	public Questionaire(String name, Period period, Question<?>...questions) {
		super();
		this.name = name;
		this.period = period == null ? new Period() : period;
		this.questions = questions == null ? new HashSet<Question<?>>() : new HashSet<Question<?>>(Arrays.asList(questions));
	}
	
	public static Questionaire EMPTY(final QuestionaireConfiguration configuration) {
		return new Questionaire();
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public Period getPeriod() { return period; }
	public void setPeriod(Period period) { this.period = period; }
	
	public int getNoOfQuestions() { return getQuestions().size(); }
	
	public LocalDate getStartDate() { return period != null ? period.getStart() : null; }
	public void setStartDate(final LocalDate start) { 
		if (period == null) {
			period = Period.starts(start);
		} else {
			period.setStart(start);
		}	
	}
	
	public LocalDate getEndDate() { return period != null ? period.getEnd() : null; }
	
	public void setEndDate(final LocalDate end) { 
		if (period == null) {
			period = Period.starts((Date) null);
		}
		
		period.setEnd(end);
	}
	
	public Set<Question<?>> getQuestions() { return questions; }
	public void setQuestions(Set<Question<?>> questions) { this.questions = questions; }
	
	public List<Question<?>> getQuestionList() {
		List<Question<?>> result = new ArrayList<>(questions.size());
		for (Question<?> question : getQuestions()) {
			result.add(question);
		}
		return result;
	}

	@CreatedBy
	private User createdBy;
	@LastModifiedBy
	private User lastModifiedBy;
	@CreatedDate
	private DateTime createdDate;
	@LastModifiedDate
	private DateTime lastModifiedDate;
	
	@Override
	public String getId() { return this.id; }
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
	
	public static Questionaire name(final String name) {
		return new Questionaire(name);
	}
	
//	public Questionaire startingOn(final LocalDate date) {
//		return startingOn(new Date(date.toEpochDay()));
//	}
	
	public Questionaire startingOn(final LocalDate date) {
		if (this.getPeriod() == null) {
			this.setPeriod(Period.starts(date));
		} else {
			this.getPeriod().setStart(date);
		}
		return this;
	}
	
//	public Questionaire closingOn(final LocalDate date) {
//		return closingOn(new Date(date.toEpochDay()));
//	}
	
	public Questionaire closingOn(final LocalDate date) {
		if (this.getPeriod() == null) {
			this.setPeriod(Period.starts((Date) null).ends(date));
		} else {
			this.getPeriod().setEnd(date);
		}
		return this;
	}
	
	public Questionaire addQuestion(final Question<?> question) {
		if (this.questions == null) {
			this.questions = new HashSet<>();
		}
		this.questions.add(question);
		
		return this;
	}
	
	public Auditable<User, String> audit(final User user) {
		if (isNew()) {
			setCreatedBy(user);
			setCreatedDate(DateTime.now());
		} else {
			setLastModifiedBy(user);
			setLastModifiedDate(DateTime.now());
		}
		
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) {return false; }
		
		Questionaire other = (Questionaire) obj;
		return id != null && other.id != null && id.equals(other.id);
	}
	
	@Override
	public String toString() {
		return String.format(
				"Questionaire [id=%s, name=%s, period=%s, questions=%s, createdBy=%s, lastModifiedBy=%s, createdDate=%s, lastModifiedDate=%s]",
				id, name, period, questions, createdBy, lastModifiedBy, createdDate, lastModifiedDate);
	}
}
