package za.co.yellowfire.threesixty.ui;

public interface Style {

	interface Percentage {
		String _100 = "100%";
	}
	
	interface Button {
		String ICON_EDIT = "icon-edit";
	}
	
	interface Text {
		String BOLDED = "bolded";
		String ITALICIZED = "italicized";
	}
	
	interface Notification {
		String ICON = "notification-icon";
		String HEADER = "notification-header";
		String ITEM = "notification-item";
		String TITLE = "notification-title";
		String CONTENT = "notification-content";
		String TIME = "notification-time";
		String STYLE_UNREAD = "unread";
	}
	
	interface Kudos {
	
		interface Received {
			String HEADER = "kudos-received-header";
			String ITEM = "kudos-received-item";
			String IMAGE = "kudos-received-image";
		}
	}
	
	interface Rating {
		String PANEL = "rating-panel";
		String FIELDS = "fields";
		String BUTTONS = "buttons";
	}
	
	interface AssessmentRating {
		String HEADER = "assessment-rating-header";
		String ROW = "assessment-rating-row";
		String ROW_ODD = "assessment-rating-row-odd";
	}
	
	interface Organization {
		String HEADER = "organization-header";
	}
}
