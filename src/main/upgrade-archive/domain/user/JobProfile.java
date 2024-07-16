package za.co.yellowfire.threesixty.domain.user;

import jakarta.persistence.Entity;
import za.co.yellowfire.threesixty.domain.AbstractAuditable;

import java.util.Objects;

@Entity
public class JobProfile extends AbstractAuditable {


	public JobProfile(String id) {
		super(id);
	}

	public String toString() {
		return Objects.toString(getId());
	}
}
