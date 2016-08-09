package za.co.yellowfire.threesixty.domain.kudos;

import za.co.yellowfire.threesixty.domain.user.User;

public interface KudosRepositoryCustom {
	/**
	 * Calculates the balance of the user's kudos wallet from the kudos received and given
	 * @param user The user of the wallet
	 * @return The wallet balance
	 */
	KudosWallet getWallet(final User user);
}
