package za.co.yellowfire.threesixty.ui.view.security;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.domain.user.UserService;

@SpringView(name = ChangePasswordView.VIEW_NAME)
public class ChangePasswordView extends HorizontalLayout implements View {
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_NAME = "changePassword";
	//private ChangePasswordForm form;
	
	@Autowired
    public ChangePasswordView(final UserService userService) {
        setSizeFull();
        
//        form = new ChangePasswordForm(userService);
//        addComponent(form);
//        setComponentAlignment(form, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeEvent event) {
    	//this.form.setEntity(new ChangePasswordModel());
    }
}