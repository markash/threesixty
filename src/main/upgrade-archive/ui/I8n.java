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
		String UPLOAD = "Upload";
		String IMPORT = "Import";
		String OK = "OK";
		String CHOOSE_FILE = "Choose File...";
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
	

	public interface Confirmation {
		String TITLE = "Confirmation";
		String YES = "Yes";
		String NO = "No";
		String OK = "OK";
		String CANCEL = "Cancel";
	}
	public interface Notifications {
		String HEADER = "Notifications";
		String BUTTON_VIEW_ALL = "View All Notifications";
		
		public interface Columns {
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
		String SELECT_PERIOD = "Select Period";

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
		String EDIT_VIEW = "objective";
		String SEARCH_VIEW = "objectives";

        interface Columns {
            String ID = "Id";
            String NAME = "Name";
            String TEXT = "Text";
            String ACTIVE = "Active";
        }

		interface Validation {
			String NAME_REQUIRED = "The objective name is required.";
		}
	}

	public interface Discipline {
		String SINGULAR = "Discipline";
		String PLURAL = "Disciplines";

		interface Columns {
			String ID = "Id";
			String NAME = "Name";
			String TEXT = "Text";
			String ACTIVE = "Active";
		}

		interface Validation {
			String NAME_REQUIRED = "The discipline name is required.";
		}
	}

	public interface Assessment {
		VaadinIcons ICON = VaadinIcons.BRIEFCASE;
		String SINGULAR = "Assessment";
		String PLURAL = "Assessments";
		
		interface Columns {
			String ID = "#";
			String EMPLOYEE = "Employee";
			String PERIOD = "Period";
			String SCORE = "Overall Score";
			String STATUS = "Status";
		}
		interface Fields {
			String MANAGER = "Manager";
			String EMPLOYEE = "Employee";
			String PERIOD = "Period";
		}
		interface Rating {
			String SINGULAR = "Assessment Rating";
			String PLURAL = "Assessment Ratings";
		}
		interface Confirmation {
			String PUBLISH = "Publishing the assessment will inform the employee that the assessment is ready for review " +
					"and further changes are not possible.\nWould you like to publish the assessment?";
			String EMPLOYEE_COMPLETE = "Completing the self-review will inform the manager that the assessment is ready for review " +
					"and further changes are not possible.\nWould you like to proceed?";
			String MANAGER_COMPLETE = "Completing the manager review will inform the employee that the assessment is ready for final review " +
					"and further changes are not possible.\nWould you like to proceed?";
			String REVIEW_COMPLETE = "Completing the final review will close off the assessment " +
					"and further changes are not possible.\nWould you like to proceed?";
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

	public interface Profile {

	    String PICTURE = "Profile picture";
    }

	public interface User {
		VaadinIcons ICON = VaadinIcons.USERS;
		String SINGULAR = "User";
		String PLURAL = "Users";
		String IMPORT = "Import "+ PLURAL;
		String LOGOUT = "Logout";

		interface Columns {
			String ID = "User Name";
			String EMAIL = "Email";
			String PHONE = "Phone";
			String FIRST_NAME = "First Name";
			String LAST_NAME = "Last Name";
			String WEBSITE = "Website";
			String REPORTS_TO = "Reports To";
			String ROLE = "Role";
			String ACTIVE = "Active";
			String IMPORT_STATUS = "Status";
		}

        interface Validation {
            String FIRST_NAME_REQUIRED = Columns.FIRST_NAME + " is required.";
            String LAST_NAME_REQUIRED = Columns.LAST_NAME + " is required.";
        }
	}
	
	public interface Identity {
		VaadinIcons ICON = VaadinIcons.SITEMAP;
		String SINGULAR = "Identity";
		String PLURAL = "Identities";
		
		interface Fields {
			String NAME = "Name";
			String TYPE = "Type";
			String LEVELS = "Levels";
			String USERS = "Users";
		}
		interface Level {
			VaadinIcons GLOBAL = VaadinIcons.GLOBE;
			VaadinIcons TENANT = VaadinIcons.RECORDS;
			VaadinIcons ORGANIZATION = VaadinIcons.AUTOMATION;
			VaadinIcons GROUP = VaadinIcons.INSTITUTION;
			VaadinIcons REGION = VaadinIcons.FLAG;
			VaadinIcons DIVISION = VaadinIcons.BUILDING;
			VaadinIcons DEPARTMENT = VaadinIcons.CUBES;
			VaadinIcons TEAM = VaadinIcons.CUBE;
			VaadinIcons INDIVIDUAL = VaadinIcons.USER;
			VaadinIcons OTHER = VaadinIcons.FOLDER;
		}
	}
}
