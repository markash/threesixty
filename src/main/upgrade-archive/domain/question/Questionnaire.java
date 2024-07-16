package za.co.yellowfire.threesixty.domain.question;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

import za.co.yellowfire.threesixty.domain.AbstractAuditable;
import za.co.yellowfire.threesixty.domain.rating.Period;

/**
 * A set of questions
 * @author Mark P Ashworth
 * @version 0.0.1
 */
@Entity
@AccessType(Type.FIELD)
public class Questionnaire extends AbstractAuditable {

	@NotNull
	private String name;
	@ManyToOne
	@JoinColumn(name = "periodId")
	private Period period; 
	private Set<Question<?>> questions;

	public Questionnaire() {
		this(null, null, (Set<Question<?>>) null);
	}

	public Questionnaire(final String name) {
		this(name, null, (Set<Question<?>>) null);
	}
	
	public Questionnaire(String name, Period period, Set<Question<?>> questions) {
		super();
		this.name = name;
		this.period = period == null ? new Period() : period;
		this.questions = questions == null ? new HashSet<Question<?>>() : questions;
	}

	public Questionnaire(String name, Period period, Question<?>...questions) {
		super();
		this.name = name;
		this.period = period == null ? new Period() : period;
		this.questions = questions == null ? new HashSet<Question<?>>() : new HashSet<Question<?>>(Arrays.asList(questions));
	}
	
	public static Questionnaire EMPTY(final QuestionaireConfiguration configuration) {
		return new Questionnaire();
	}

	public static Questionnaire ID(final String id) {
		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setId(id);
		return questionnaire;
	}

	public static Questionnaire NAME(final String name) {
		return new Questionnaire(name);
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
		result.addAll(getQuestions());
		return result;
	}




	
//	public Questionaire startingOn(final LocalDate date) {
//		return startingOn(new Date(date.toEpochDay()));
//	}
	
	public Questionnaire startingOn(final LocalDate date) {
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
	
	public Questionnaire closingOn(final LocalDate date) {
		if (this.getPeriod() == null) {
			this.setPeriod(Period.starts((Date) null).ends(date));
		} else {
			this.getPeriod().setEnd(date);
		}
		return this;
	}
	
	public Questionnaire addQuestion(final Question<?> question) {
		if (this.questions == null) {
			this.questions = new HashSet<>();
		}
		this.questions.add(question);
		
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) {return false; }
		
		Questionnaire other = (Questionnaire) obj;
		return getId() != null && other.getId() != null && getId().equals(other.getId());
	}
	
	@Override
	public String toString() {
		return String.format(
				"Questionnaire [id=%s, name=%s, period=%s, questions=%s, createdBy=%s, lastModifiedBy=%s, createdDate=%s, lastModifiedDate=%s]",
				getId(), name, period, questions, getCreatedBy(), getLastModifiedBy(), getCreatedDate(), getLastModifiedDate());
	}
}
