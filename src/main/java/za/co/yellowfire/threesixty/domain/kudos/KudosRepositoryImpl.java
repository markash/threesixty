package za.co.yellowfire.threesixty.domain.kudos;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;

import za.co.yellowfire.threesixty.domain.user.User;

public class KudosRepositoryImpl implements KudosRepositoryCustom {

	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public KudosRepositoryImpl(final MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public KudosWallet getWallet(final User account) {
		if (account == null) { throw new IllegalArgumentException("The user account of the wallet is null"); }
		
		BasicQuery query = new BasicQuery("{$or: [{'recipient.$id': '" + account.getId() + "'}, {'donor.$id': '" + account.getId() + "'}]}");
		List<Kudos> kudos = mongoTemplate.find(query, Kudos.class);
		
		KudosWallet wallet = new KudosWallet();
		kudos.stream()
			 /* Map the Kudos to a KudosBalance taking into account the user to determine the BalanceType*/
			 .map(k -> new KudosBalance(account, k))
			 /* Group the KudosBalances by type*/
			 .collect(Collectors.groupingBy(kb -> kb.getType()))
			 /* For each KudosBalance in the group, reduce by adding the right to the left and getting the value from the Optional */
			 .entrySet().stream().map(e -> e.getValue().stream().reduce((l, r) -> l.add(r))).map(f -> f.get())
			 /* Collect the KudosBalances in the KudosWallet*/
			 .forEach(balance -> wallet.add(balance));
		     
		return wallet;
	}
}
