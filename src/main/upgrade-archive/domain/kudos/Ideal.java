package za.co.yellowfire.threesixty.domain.kudos;

import jakarta.persistence.Entity;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

import za.co.yellowfire.threesixty.domain.AbstractAuditable;

import java.util.Objects;

@Entity
@AccessType(Type.FIELD)
public class Ideal extends AbstractAuditable {
	public static Ideal EMPTY() { return new Ideal(); }
	
	private String description;

    public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Ideal other = (Ideal) obj;
		if (getId() == null) {
			return other.getId() == null;
		} else {
			return getId().equals(other.getId());
		}
	}
	
	@Override
	public String toString() {
		return Objects.toString(this.getId());
	}
}
