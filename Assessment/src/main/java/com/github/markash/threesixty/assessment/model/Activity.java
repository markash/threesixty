package com.github.markash.threesixty.assessment.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Mark P Ashworth
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
public class Activity extends AbstractAuditable<User, Long> {

	@Enumerated(EnumType.STRING)
	@Column(name = "activity_type")
	private ActivityTypes type;

	private boolean active = true;

	public Activity() { }

	public Activity(
			final ActivityTypes type) {
		this.type = type;
	}

	public ActivityTypes getType() { return type; }
	public void setType(ActivityTypes type) { this.type = type; }

	@Transient
	public List<LocalDateTime> getTimelineDates() { return new ArrayList<>(); }

	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode());
	}

	@Override
	public String toString() {
		return "Activity {" +
				"type=" + type +
				"---" +
				super.toString() +
				'}';
	}
}
