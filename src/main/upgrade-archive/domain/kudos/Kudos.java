package za.co.yellowfire.threesixty.domain.kudos;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;


import za.co.yellowfire.threesixty.domain.AbstractAuditable;
import za.co.yellowfire.threesixty.domain.user.User;

@Entity
@AccessType(Type.FIELD)
public class Kudos extends AbstractAuditable {
	
	public static Kudos EMPTY() { return new Kudos(); }
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_MESSAGE = "message";
	public static final String FIELD_BADGE = "badge";
	public static final String FIELD_DONOR = "donor";
	public static final String FIELD_RECIPIENT = "recipient";
	public static final String FIELD_ACTIVE = "active";

	private String message;
	@ManyToOne
	@JoinColumn(name = "badgeId")
	private Badge badge;
	/* The donor of the kudos */
	@ManyToOne
	@JoinColumn(name = "donorId")
	private User donor;
	/* The recipient of the kudos */
	@ManyToOne
	@JoinColumn(name = "recipientId")
	private User recipient;

    public String getMessage() { return this.message; }
	public void setMessage(final String message) { this.message = message; }
	
	public Badge getBadge() { return badge; }
	public void setBadge(final Badge badge) { this.badge = badge; }
	
	public User getDonor() { return this.donor; }
	public void setDonor(final User donor) { this.donor = donor; }
	
	public User getRecipient() { return this.recipient; }
	public void setRecipient(final User recipient) { this.recipient = recipient; }
}
