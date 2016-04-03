package za.co.yellowfire.threesixty.ui;

import com.vaadin.server.FontAwesome;

public class I8n {
	
	public interface Format {
		String DATE = "yyyy-MM-dd";
		String TIME = "HH:MM:ss";
		String DATE_TIME = DATE + " " + TIME;
	}
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
			String PICTURE = "Picture";
		}
		
		public interface Received {
			String NONE = "No kudos received as yet.";
		}
	}
	
	public interface Period {
		String SINGULAR = "Period";
		String PLURAL = "Periods";
		
		public interface Columns {
			String ID = "#";
			String START = "Start Date";
			String END = "End Date";
			String DEADLINE_PUBLISH = "Publish Deadline";
			String DEADLINE_COMPLETE = "Complete Deadline";
			String DEADLINE_SELF_ASSESSMENT = "Self Assessment Deadline";
			String DEADLINE_ASSESSOR_ASSESSMENT = "Manager Assessment Deadline";
			String ACTIVE = "Active";
		}
		
		public interface Fields {
			String START = I8n.Period.Columns.START;
			String END = I8n.Period.Columns.END;
			String DEADLINE_PUBLISH = I8n.Period.Columns.DEADLINE_PUBLISH;
			String DEADLINE_COMPLETE = I8n.Period.Columns.DEADLINE_COMPLETE;
			String DEADLINE_SELF_ASSESSMENT = I8n.Period.Columns.DEADLINE_SELF_ASSESSMENT;
			String DEADLINE_ASSESSOR_ASSESSMENT = I8n.Period.Columns.DEADLINE_ASSESSOR_ASSESSMENT;
			String ACTIVE = I8n.Period.Columns.ACTIVE;
		}
	}
	
	public interface Assessment {
		FontAwesome ICON = FontAwesome.BRIEFCASE;
		String SINGULAR = "Assessment";
		String PLURAL = "Assessments";
		
		public interface Columns {
			String ID = "#";
			String EMPLOYEE = "Employee";
			String PERIOD = "Period";
			String SCORE = "Overall Score";
			String STATUS = "Status";
		}
		public interface Fields {
			String MANAGER = "Manager";
			String EMPLOYEE = "Employee";
			String PERIOD = "Period";
		}
	}
	
	public interface User {
		FontAwesome ICON = FontAwesome.USERS;
		String SINGULAR = "User";
		String PLURAL = "Users";
	}
}
