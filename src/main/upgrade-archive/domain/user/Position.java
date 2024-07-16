package za.co.yellowfire.threesixty.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.apache.commons.lang3.StringUtils;
import za.co.yellowfire.threesixty.domain.AbstractAuditable;

import java.util.Objects;

@Entity
public class Position extends AbstractAuditable {

	@ManyToOne
	@JoinColumn(name = "jobProfileId")
	private JobProfile jobProfile;

	public Position(String id, JobProfile jobProfile) {
		super(id);
		this.jobProfile = jobProfile;
	}

	public JobProfile getJobProfile() { return jobProfile; }
	public void setJobProfile(JobProfile jobProfile) { this.jobProfile = jobProfile; }

	public String toString() {
		final String profileName = jobProfile != null && !StringUtils.isBlank(jobProfile.getId()) ? jobProfile.getId() : "";
		return Objects.toString(getId(), "") + " : " + profileName;
	}
}
