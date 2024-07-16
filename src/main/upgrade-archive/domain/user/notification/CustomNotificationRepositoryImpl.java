package za.co.yellowfire.threesixty.domain.user.notification;

import java.util.ArrayList;
import java.util.List;

import za.co.yellowfire.threesixty.domain.user.User;

@SuppressWarnings("unused")
public class CustomNotificationRepositoryImpl implements CustomNotificationRepository {

//	@Autowired
//	private MongoTemplate template;
//
	@Override
	public List<NotificationSummary> getNotificationSummaries(User user) {
		
//		Aggregation aggregation = Aggregation.newAggregation(
//				Aggregation.match(Criteria.where("user").is("DBRef('user', '" + user.getId() + "')")),
//				Aggregation.group("category").count().as("count"),
//				Aggregation.project("count").and("category").previousOperation(),
//				Aggregation.sort(Sort.Direction.DESC, "category")
//				);
//
//		return template.aggregate(
//						aggregation,
//						UserNotification.class,
//						NotificationSummary.class).getMappedResults();

		return new ArrayList<>();
	}
}
