package za.co.yellowfire.threesixty.domain.kudos;

import java.io.Serializable;

import za.co.yellowfire.threesixty.domain.user.User;

/**
 * A kudos balance
 * @author Mark P Ashworth
 */
public class KudosBalance implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_VALUE = "value";
	public static final String FIELD_COUNT = "count";
	
	private KudosBalanceType type;
	private int value;
	private int count;
	
	public KudosBalance(KudosBalanceType type) {
		this.type = type;
	}
	
	public KudosBalance(KudosBalanceType type, int value, int count) {
		this.type = type;
		this.value = value;
		this.count = count;
	}

	/**
	 * Constructs the receipt and donation balance types from the kudos
	 * @param account The user of the wallet
	 * @param kudos The kudos to account for, i.e. a donation by the user is a negative value and a receipt is positive
	 */
	public KudosBalance(final User account, final Kudos kudos) {
		if (account == null) { throw new IllegalArgumentException("The account is null"); }
		if (kudos == null) { throw new IllegalArgumentException("The kudos is null"); }
		
		if (kudos.getDonor() != null && account.equals(kudos.getDonor())) { 
			this.type = KudosBalanceType.Donations;
			this.value = kudos.getBadge() != null && kudos.getBadge().getValue() != null ? kudos.getBadge().getValue() * -1 : 0;
			this.count++;
		} else  if (kudos.getRecipient() != null && account.equals(kudos.getRecipient())) {
			this.type = KudosBalanceType.Receipts;
			this.value = kudos.getBadge() != null && kudos.getBadge().getValue() != null ? kudos.getBadge().getValue() : 0;
			this.count++;
		}
	}
	
	public KudosBalance add(final KudosBalance balance) {
		return new KudosBalance(
				this.type, 
				this.value + (balance != null ? balance.getValue() : 0), 
				this.count + (balance != null ? balance.getCount() : 0));
	}
	
	public KudosBalanceType getType() { return type; }
	public int getValue() { return value; }
	public int getCount() { return count; }
}
