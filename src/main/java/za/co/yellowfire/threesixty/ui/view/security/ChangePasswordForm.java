package za.co.yellowfire.threesixty.ui.view.security;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.v7.fields.MPasswordField;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.FormHeader;

public class ChangePasswordForm extends AbstractForm<ChangePasswordModel> {
	private static final long serialVersionUID = 7406004850010260935L;
	
	public static final String VIEW_NAME = "changePassword";
	private static final String LABEL_CHANGE_PASSWORD = "Change Password";
	private static final String LABEL_APPLICATION_NAME = "Three <strong>Sixty</strong>";
	
	private MPasswordField oldPassword = new MPasswordField(I8n.ChangePassword.Fields.OLD_PASSWORD)
			.withIcon(VaadinIcons.LOCK)
			.withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
			.withWidth(Style.Percentage._100);
	private MPasswordField newPassword = new MPasswordField(I8n.ChangePassword.Fields.NEW_PASSWORD)
			.withIcon(VaadinIcons.LOCK)
			.withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
			.withWidth(Style.Percentage._100);
	private MPasswordField confirmPassword = new MPasswordField(I8n.ChangePassword.Fields.CONFIRM_PASSWORD)
			.withIcon(VaadinIcons.LOCK)
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

	public ChangePasswordForm(
	        final ChangePasswordHandler changePasswordHandler) {
		super(ChangePasswordModel.class);

		this.getBinder().withValidator(new ConfirmPasswordValidator());
		this.setSaveButton(changePasswordButton);
		this.setSavedHandler(changePasswordHandler);
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
}
