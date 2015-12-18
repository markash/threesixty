package za.co.yellowfire.threesixty.domain.user.notification;

public class NotificationSummary {

	private NotificationCategory category;
	private int count;
	
	public NotificationCategory getCategory() { return category; }
	public void setCategory(NotificationCategory category) { this.category = category; }
	
	public int getCount() { return count; }
	public void setCount(int count) { this.count = count; }
	
	public String getTitle() {
		return getCount() + " " + (getCategory() != null ? getCategory().name() : "null") + " events";
	}
}
