package za.co.yellowfire.threesixty.ui.view.security;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.ui.I8n;

@SideBarItem(sectionId = Sections.PROFILE, caption = ChangePasswordView.TITLE)
@VaadinFontIcon(VaadinIcons.SAFE_LOCK)
@SpringView(name = ChangePasswordView.VIEW_NAME)
public class ChangePasswordView extends HorizontalLayout implements View {
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_NAME = "changePassword";
	public static final String TITLE = I8n.ChangePassword.SINGULAR;

	private final ChangePasswordForm changePasswordForm;

	@Autowired
    public ChangePasswordView(final ChangePasswordForm changePasswordForm) {
        setSizeFull();

        this.changePasswordForm = changePasswordForm;

        addComponent(changePasswordForm);
        setComponentAlignment(changePasswordForm, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        this.changePasswordForm.getBinder().readBean(new ChangePasswordModel());
    }
}