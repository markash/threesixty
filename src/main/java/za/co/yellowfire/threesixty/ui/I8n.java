package za.co.yellowfire.threesixty.ui;

import com.vaadin.icons.VaadinIcons;

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
		String CHANGE_PASSWORD = "Change Password";
		String RESET_PASSWORD = "Reset Password";
	}
	
	public interface Fields {
		String ID = "Id";
		String CREATED_TIME = "Created at";
		String MODIFIED_TIME = "Modified at";
		String CREATED_BY = "Created by";
		String MODIFIED_BY = "Modified by";
		String DESCRIPTION = "Description";
		String MOTIVATION = "Motivation";
	}
	
	public interface Styles {
		String LOGIN_PANEL = "login-panel";
		String FIELDS = "fields";
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

	public interface Dashboard {
		String SINGULAR = "Dashboard";
	}

	public interface Ideal {	
		String SINGULAR = "Ideal";
		String PLURAL = "Ideals";
		public interface Fields {
			String IDEAL = "Ideal";
		}
	}
	
	public interface Badge {
		String SINGULAR = "Badge";
		String PLURAL = "Badges";
		public interface Fields {
			String DESCRIPTION = I8n.Fields.DESCRIPTION;
			String IDEAL = I8n.Ideal.SINGULAR;
			String MOTIVATION = "Motivation";
			String VALUE = "Value";
		}
	}
	
	public interface Kudos {
		String SINGULAR = "Kudos";
		String PLURAL = "Kudos";
		
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
		
		public interface Wallet {
			String CURRENCY_SYMBOL = "$";
		}
	}
	
	public interface Period {
		String SINGULAR = "Period";
		String PLURAL = "Periods";
		
		interface Columns {
			String ID = "#";
			String START = "Start Date";
			String END = "End Date";
			String DEADLINE_PUBLISH = "Publish Deadline";
			String DEADLINE_COMPLETE = "Complete Deadline";
			String DEADLINE_SELF_ASSESSMENT = "Self Assessment Deadline";
			String DEADLINE_ASSESSOR_ASSESSMENT = "Manager Assessment Deadline";
			String ACTIVE = "Active";
		}

		interface Validation {
			String START_REQUIRED = Columns.START + " is required.";
			String END_REQUIRED = Columns.END + " is required.";
		}

		interface Fields {
			String START = I8n.Period.Columns.START;
			String END = I8n.Period.Columns.END;
			String DEADLINE_PUBLISH = I8n.Period.Columns.DEADLINE_PUBLISH;
			String DEADLINE_COMPLETE = I8n.Period.Columns.DEADLINE_COMPLETE;
			String DEADLINE_SELF_ASSESSMENT = I8n.Period.Columns.DEADLINE_SELF_ASSESSMENT;
			String DEADLINE_ASSESSOR_ASSESSMENT = I8n.Period.Columns.DEADLINE_ASSESSOR_ASSESSMENT;
			String ACTIVE = I8n.Period.Columns.ACTIVE;
		}
	}

	public interface Objective {
		String SINGULAR = "Objective";
		String PLURAL = "Objectives";

        interface Columns {
            String ID = "Name";
            String ACTIVE = "Active";
        }
	}

	public interface Assessment {
		VaadinIcons ICON = VaadinIcons.BRIEFCASE;
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
	
	public interface UserNotification {
		interface Fields {
			String CREATED_BY = "Created By";
			String MODIFIED_BY = "Modified By";
		}
	}
	public interface ChangePassword {
		String SINGULAR = "Change Password";
		String PLURAL = "Change Passwords";

		interface Fields {
			String OLD_PASSWORD = "Old Password";
			String NEW_PASSWORD = "New Password";
			String CONFIRM_PASSWORD = "Confirm Password";
		}
		
		interface Errors {
			String PASSWORDS_DO_NOT_MATCH = "The new and confirm passwords do not match";
		}
	}
	
	public interface User {
		VaadinIcons ICON = VaadinIcons.USERS;
		String SINGULAR = "User";
		String PLURAL = "Users";
	}
	
	public interface Organization {
		VaadinIcons ICON = VaadinIcons.SITEMAP;
		String SINGULAR = "Organization";
		String PLURAL = "Organization";
		
		interface Fields {
			String NAME = "Name";
			String TYPE = "Type";
			String LEVELS = "Levels";
			String USERS = "Users";
		}
		interface Level {
			VaadinIcons ORGANIZATION = Organization.ICON;
			VaadinIcons GROUP = VaadinIcons.INSTITUTION;
			VaadinIcons REGION = VaadinIcons.FLAG;
			VaadinIcons DIVISION = VaadinIcons.BUILDING;
			VaadinIcons DEPARTMENT = VaadinIcons.CUBES;
			VaadinIcons TEAM = VaadinIcons.CUBE;
			VaadinIcons OTHER = VaadinIcons.FOLDER;
		}
	}
}
