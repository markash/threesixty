package za.co.yellowfire.threesixty.ui.view.security;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.FormHeader;

public class ChangePasswordForm extends AbstractForm<ChangePasswordModel> {
	private static final long serialVersionUID = 7406004850010260935L;
	
	public static final String VIEW_NAME = "changePassword";
	private static final String LABEL_CHANGE_PASSWORD = "Change Password";
	private static final String LABEL_APPLICATION_NAME = "Three <strong>Sixty</strong>";
	
	private PasswordField oldPassword = new PasswordField(I8n.ChangePassword.Fields.OLD_PASSWORD);
	private PasswordField newPassword = new PasswordField(I8n.ChangePassword.Fields.NEW_PASSWORD);
	private PasswordField confirmPassword = new PasswordField(I8n.ChangePassword.Fields.CONFIRM_PASSWORD);

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

	public ChangePasswordForm(
	        final ChangePasswordHandler changePasswordHandler) {
		super(ChangePasswordModel.class);

		this.oldPassword.setIcon(VaadinIcons.LOCK);
		this.oldPassword.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		this.oldPassword.setWidth(Style.Percentage._100);

		this.newPassword.setIcon(VaadinIcons.LOCK);
        this.newPassword.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.newPassword.setWidth(Style.Percentage._100);

        this.confirmPassword.setIcon(VaadinIcons.LOCK);
        this.confirmPassword.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.confirmPassword.setWidth(Style.Percentage._100);

		this.getBinder().withValidator(new ConfirmPasswordValidator());
		this.setSaveButton(changePasswordButton);
		this.setSavedHandler(changePasswordHandler);
	}
	
	@Override
	protected Component createContent() {
		final MVerticalLayout layout = new MVerticalLayout()
				.withFullWidth()
				.withSpacing(true)
				.withMargin(false)
				.with(formHeader, fields)
				.withResponsive(true)
				.withAlign(formHeader, Alignment.MIDDLE_CENTER);
		
		setStyleName(I8n.Styles.LOGIN_PANEL);
		setSizeUndefined();
        return layout;
	}
}
