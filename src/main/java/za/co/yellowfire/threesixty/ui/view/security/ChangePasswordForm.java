package za.co.yellowfire.threesixty.ui.view.security;

import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.MBeanFieldGroup.MValidator;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MPasswordField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.Response;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.DashboardEvent.UserChangePasswordEvent;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.FormHeader;
import za.co.yellowfire.threesixty.ui.component.notification.NotificationBuilder;

public class ChangePasswordForm extends AbstractForm<ChangePasswordModel> {
	private static final long serialVersionUID = 7406004850010260935L;
	
	public static final String VIEW_NAME = "changePassword";
	private static final String LABEL_CHANGE_PASSWORD = "Change Password";
	private static final String LABEL_APPLICATION_NAME = "Three <strong>Sixty</strong>";
	
	private MPasswordField oldPassword = new MPasswordField(I8n.ChangePassword.Fields.OLD_PASSWORD)
			.withIcon(FontAwesome.LOCK)
			.withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
			.withWidth(Style.Percentage._100);
	private MPasswordField newPassword = new MPasswordField(I8n.ChangePassword.Fields.NEW_PASSWORD)
			.withIcon(FontAwesome.LOCK)
			.withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
			.withWidth(Style.Percentage._100);
	private MPasswordField confirmPassword = new MPasswordField(I8n.ChangePassword.Fields.CONFIRM_PASSWORD)
			.withIcon(FontAwesome.LOCK)
			.withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
			.withWidth(Style.Percentage._100);
	private MButton changePasswordButton = new MButton(I8n.Button.CHANGE_PASSWORD)
			.withStyleName(ValoTheme.BUTTON_PRIMARY)
			.withClickShortcut(KeyCode.ENTER)
			.withWidth(Style.Percentage._100);
	private FormHeader formHeader = new FormHeader(LABEL_CHANGE_PASSWORD, LABEL_APPLICATION_NAME);
	private MVerticalLayout fields = new MVerticalLayout()
			.withSpacing(true)
			.withMargin(false)
			.withStyleName(I8n.Styles.FIELDS)
			.with(oldPassword, newPassword, confirmPassword, changePasswordButton);
	
	private MValidator<ChangePasswordModel> validator;
	
	public ChangePasswordForm(final UserService userService) {
		this.validator = new ConfirmPasswordValidator();
		this.addValidator(validator, newPassword, confirmPassword);
		this.setSaveButton(changePasswordButton);
		this.setSavedHandler(new ChangePasswordHandler(userService));
	}
	
	@Override
	protected Component createContent() {
		final MVerticalLayout layout = new MVerticalLayout()
				.withWidthUndefined()
				.withSpacing(true)
				.withMargin(false)
				.with(formHeader, fields)
				.withResponsive(true)
				.withAlign(formHeader, Alignment.MIDDLE_CENTER);
		
		setStyleName(I8n.Styles.LOGIN_PANEL);
		setSizeUndefined();
        return layout;
	}

	@SuppressWarnings("serial")
	private static class ChangePasswordHandler implements SavedHandler<ChangePasswordModel> {

		private UserService userService;
		
		public ChangePasswordHandler(final UserService userService) {
			this.userService = userService;
		}

		@Override
		public void onSave(ChangePasswordModel model) {
			
	    	User user = userService.getCurrentUser();
	    	Response<User> response = userService.changePassword(user.getId(), model.getOldPassword(), model.getNewPassword());
	    	switch (response.getResult()) {
	        	case OK: 
	        		DashboardEventBus.post(new UserChangePasswordEvent(response.getValue()));
	        		break;
	        	case UNAUTHORIZED: 
	        		NotificationBuilder.showNotification("Unable to authenticate", "The user name and password provided is incorrect.", 20000); 
	        		break;
	    	}	
		}
	}
	
	@SuppressWarnings("serial")
	private static class ConfirmPasswordValidator implements MBeanFieldGroup.MValidator<ChangePasswordModel>  {
		@Override
		public void validate(final ChangePasswordModel model) throws InvalidValueException {
			if (!model.hasMatchingPasswords()) {
				throw new Validator.InvalidValueException("End time cannot be before start time!");
			}
		}
	}
}
