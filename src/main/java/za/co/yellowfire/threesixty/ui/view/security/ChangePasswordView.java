package za.co.yellowfire.threesixty.ui.view.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MPasswordField;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.Response;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.DashboardEvent.UserChangePasswordEvent;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.field.MLabel;
import za.co.yellowfire.threesixty.ui.component.notification.NotificationBuilder;

@SpringView(name = ChangePasswordView.VIEW_NAME)
public class ChangePasswordView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_NAME = "changePassword";
	private static final String LABEL_CHANGE_PASSWORD = "Change Password";
	private static final String LABEL_APPLICATION_NAME = "Three <strong>Sixty</strong>";
	
	private MPasswordField oldPassword = new MPasswordField(I8n.ChangePassword.Fields.OLD_PASSWORD)
			.withIcon(FontAwesome.LOCK)
			.withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
	private MPasswordField newPassword = new MPasswordField(I8n.ChangePassword.Fields.NEW_PASSWORD)
			.withIcon(FontAwesome.LOCK)
			.withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
	private MPasswordField confirmPassword = new MPasswordField(I8n.ChangePassword.Fields.CONFIRM_PASSWORD)
			.withIcon(FontAwesome.LOCK)
			.withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
			.withValidator(new ConfirmPasswordValidator(newPassword));
	private MButton changePasswordButton = new MButton(I8n.Button.CHANGE_PASSWORD)
			.withStyleName(ValoTheme.BUTTON_PRIMARY)
			.withClickShortcut(KeyCode.ENTER)
			.withListener(this::onChangePassword);
	
	private UserService userService;
	
	@Autowired
    public ChangePasswordView(final UserService userService) {
        setSizeFull();
        this.userService = userService;
        
        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");
        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        return loginPanel;
    }

	private Component buildFields() {
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");
        fields.addComponents(oldPassword, newPassword, confirmPassword, changePasswordButton);
        //fields.setComponentAlignment(changePasswordButton, Alignment.BOTTOM_LEFT);
        
        changePasswordButton.focus();
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        MLabel welcome = new MLabel(LABEL_CHANGE_PASSWORD);
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label(LABEL_APPLICATION_NAME, ContentMode.HTML);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

    public void onChangePassword(final ClickEvent event) {
    	
    	User user = userService.getCurrentUser();
    	Response<User> response = userService.changePassword(user.getId(), oldPassword.getValue(), newPassword.getValue());
    	switch (response.getResult()) {
        	case OK: 
        		DashboardEventBus.post(new UserChangePasswordEvent(response.getValue()));
        		break;
        	case UNAUTHORIZED: 
        		NotificationBuilder.showNotification("Unable to authenticate", "The user name and password provided is incorrect.", 20000); 
        		break;
    	}
    }
    
    @Override
    public void enter(ViewChangeEvent event) { }
    
    @SuppressWarnings("serial")
	private static class ConfirmPasswordValidator extends AbstractValidator<String>  {
		private MPasswordField newPasswordField;
		
    	public ConfirmPasswordValidator(MPasswordField newPasswordField) {
			super(I8n.ChangePassword.Errors.PASSWORDS_DO_NOT_MATCH);
			this.newPasswordField = newPasswordField;
		}

		@Override
		protected boolean isValidValue(String value) {
			return value.equals(this.newPasswordField.getValue());
		}

		@Override
		public Class<String> getType() { return String.class; }
    }
}