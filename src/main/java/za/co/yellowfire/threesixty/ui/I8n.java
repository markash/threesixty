package za.co.yellowfire.threesixty.ui;

public class I8n {
	public interface Button {
		String SAVE = "Save";
		String RESET = "Reset";
		String DELETE = "Delete";
		String CANCEL = "Cancel";
		String NEW = "New...";
		String CHANGE = "Change...";
		String EDIT = "Edit...";
		String CLEAR_ALL = "Clear all";
	}
	
	public interface Fields {
		String ID = "Id";
		String CREATED_TIME = "Created at";
		String MODIFIED_TIME = "Modified at";
		String CREATED_BY = "Created by";
		String MODIFIED_BY = "Modified by";
	}
	
	public interface Notifications {
		String HEADER = "Notifications";
		String BUTTON_VIEW_ALL = "View All Notifications";
		
		public interface Fields {
			String ID = I8n.Fields.ID;
			String ACTION = "Action";
			String CONTENT = "Content";
			String TIME = "Time";
			String USER = "User";
			String CATEGORY = "Category";
		}
		
		public interface Views {
			public interface Edit {
				String TITLE = "Notification";
			}
			public interface Search {
				String TITLE = "Notifications";
			}
		}
	}
	
	public interface Ideal {	
		public interface Fields {
			String IDEAL = "Ideal";
		}
	}
	
	public interface Kudos {
		
		public interface Fields {
			String BADGE = "Badge";
			String RECIPIENT = "Recipient";
			String MESSAGE = "Message";
			String IMAGE = "Image";
		}
		
		public interface Received {
			String NONE = "No kudos received as yet.";
		}
	}
}
