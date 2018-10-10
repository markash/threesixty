package za.co.yellowfire.threesixty.ui.view;

import com.github.markash.ui.component.notification.NotificationBuilder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@PrototypeScope
@SpringComponent
public class LoginView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_NAME = "login";

    private final MTextField username =
            new MTextField("Username")
                    .withIcon(VaadinIcons.USER)
                    .withStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

    private final PasswordField password =
            new PasswordField("Password");

    private final MButton signIn = new MButton("Sign In")
            .withStyleName(ValoTheme.BUTTON_PRIMARY)
            .withClickShortcut(KeyCode.ENTER);

    private final VaadinSecurity vaadinSecurity;
    private final EventBus.SessionEventBus eventBus;

	@Autowired
    public LoginView(
            final VaadinSecurity vaadinSecurity,
            final EventBus.SessionEventBus eventBus) {

	    setSizeFull();
	    setStyleName("loginview");
        Responsive.makeResponsive(this);

        this.vaadinSecurity = vaadinSecurity;
        this.eventBus = eventBus;

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        NotificationBuilder.showNotification("Welcome to Three<strong>Sixty</strong>",
        		"<span>The seamless employee recognition and evaluation system.</span><br />",
        		20000,
        		true);
    }

    private Component buildLoginForm() {
        return new MVerticalLayout()
                        .withUndefinedWidth()
                        .withSpacing(true)
                        .withStyleName("login-panel")
                        .with(buildLabels(), buildFields()/*, new CheckBox("Remember me", true)*/);
    }

    @SuppressWarnings("serial")
	private Component buildFields() {
        this.username.setDescription("The users are <br />"
        		+ "<ul>"
        		+ "<li><strong>admin</strong> with password <strong>password</strong>"
        		+ "<li><strong>katie</strong> with password <strong>password</strong>"
        		+ "<li><strong>marmite</strong> with password <strong>password</strong>"
        		+ "<li><strong>brown</strong> with password <strong>password</strong>"
        		+ "<li><strong>linus</strong> with password <strong>password</strong>"
        		+ "<li><strong>lucy</strong> with password <strong>password</strong>"
        		+ "</ul>");
        
        this.password.setIcon(VaadinIcons.LOCK);
        this.password.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        this.signIn.focus();
        signIn.addClickListener(this::onSignIn);

        return new MHorizontalLayout()
                        .withSpacing(true)
                        .withStyleName("fields")
                        .with(username, password, signIn)
                        .withComponentAlignment(signIn, Alignment.BOTTOM_LEFT);
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("Three <strong>Sixty</strong>", ContentMode.HTML);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }

    @SuppressWarnings("unused")
    private void onSignIn(final ClickEvent event) {
	    try {
            final Authentication authentication = vaadinSecurity.login(username.getValue(), password.getValue());
            eventBus.publish(this, new SuccessfulLoginEvent(getUI(), authentication));
        } catch (AuthenticationException ex) {
            NotificationBuilder.showNotification(
                    "Unable to authenticate",
                    "The user name and password provided is incorrect.",
                    20000);
        } catch (Exception ex) {
            NotificationBuilder.showNotification(
                    "Unable to authenticate",
                    "Unexpected exception occurred.",
                    20000);
            LoggerFactory.getLogger(getClass()).error("Unexpected error while logging in", ex);
        }


//        Response<User> response = userService.authenticate(username.getValue(), password.getValue());
//        switch (response.getResult()) {
//            case OK:
//                DashboardEventBus.post(new UserLoginEvent(response.getValue()));
//                break;
//            case UNAUTHORIZED:
//                NotificationBuilder.showNotification(
//                        "Unable to authenticate",
//                        "The user name and password provided is incorrect.",
//                        20000);
//                break;
//        }
    }
}