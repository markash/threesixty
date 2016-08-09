package za.co.yellowfire.threesixty.domain.kudos;

import java.io.Serializable;

/**
 * The wallet of the kudos for a specific user
 * @author Mark P Ashworth
 */
public class KudosWallet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private KudosBalance balance = new KudosBalance(KudosBalanceType.Balance);
	private KudosBalance receipts = new KudosBalance(KudosBalanceType.Receipts);
	private KudosBalance donations = new KudosBalance(KudosBalanceType.Donations);
	
	/**
	 * Adds the balance to the wallet
	 * @param balance The balance to add
	 */
	public void add(final KudosBalance balance) {
		if (balance != null) {
			
			switch (balance.getType()) {
			case Receipts: 
				setReceipts(balance.getCount(), balance.getValue());
				break;
			case Donations: 
				setDonations(balance.getCount(), balance.getValue());
				break;
			case Balance:
				this.balance = balance;
			}
		}
	}
	
	/**
	 * Set the value of the kudos receipts
	 * @param count The count of kudos receipt transactions
	 * @param value The value of the kudos receipt transactions
	 */
	public void setReceipts(final int count, final int value) {
		this.receipts = new KudosBalance(KudosBalanceType.Receipts, Math.abs(value), Math.abs(count));
		updateBalance();
	}
	
	/**
	 * Set the value of the kudos donations
	 * @param count The count of kudos donate transactions
	 * @param value The value of the kudos donate transactions
	 */
	public void setDonations(final int count, final int value) {
		this.donations = new KudosBalance(KudosBalanceType.Donations, Math.abs(value), Math.abs(count));
		updateBalance();
	}
	
	private void updateBalance() {
		this.balance = 
				new KudosBalance(
						KudosBalanceType.Balance, 
						this.receipts.getValue() - this.donations.getValue(), 
						this.receipts.getCount() + this.donations.getCount());
	}

	public final KudosBalance getBalance() {
		return balance;
	}

	public final KudosBalance getReceipts() {
		return receipts;
	}

	public final KudosBalance getDonations() {
		return donations;
	}
}
