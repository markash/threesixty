package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.domain.mail.SendGridMailingService;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditView;

@SpringView(name = AssessmentEditView.VIEW_NAME)
public final class AssessmentEditView extends AbstractEntityEditView<Assessment> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Assessment";
	public static final String VIEW_NAME = "assessment";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static final String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); } 

    @Autowired
    public AssessmentEditView(
    		final AssessmentService service, 
    		final SendGridMailingService mailingService, 
    		final UserService userService) {
    	
    	super(service, 
    			new AssessmentEntityEditForm(service, mailingService, userService.getCurrentUser()),
    			userService.getCurrentUser().isAdmin()
    			);
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(AssessmentEditView.VIEW("new-assessment"));
	}
}

