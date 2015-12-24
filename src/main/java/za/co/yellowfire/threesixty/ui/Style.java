package za.co.yellowfire.threesixty.ui;

public interface Style {

	public interface Button {
		String ICON_EDIT = "icon-edit";
	}
	
	public interface Text {
		String BOLDED = "bolded";
		String ITALICIZED = "italicized";
	}
	
	public interface Notification {
		String ICON = "notification-icon";
		String HEADER = "notification-header";
		String ITEM = "notification-item";
		String TITLE = "notification-title";
		String CONTENT = "notification-content";
		String TIME = "notification-time";
		String STYLE_UNREAD = "unread";
	}
	
	public interface Kudos {
	
		public interface Received {
			String HEADER = "kudos-received-header";
			String ITEM = "kudos-received-item";
			String IMAGE = "kudos-received-image";
		}
	}
	
	public interface Rating {
		String PANEL = "rating-panel";
		String FIELDS = "fields";
		String BUTTONS = "buttons";
	}
}
